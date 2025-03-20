import PositionDAO._
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.LocalDate

sealed trait PortefeuilleCommand
case class AjouterPosition(position: Position, replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand
case class SupprimerPosition(actifId: Int, replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand
case class ObtenirValeur(replyTo: ActorRef[BigDecimal]) extends PortefeuilleCommand
// MARKET DATA
case class MiseAJourPrix(actifId: Int, nouveauPrix: BigDecimal) extends PortefeuilleCommand

case class AcheterActif(actifId: Int, quantite: Double, prix: BigDecimal, replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand
case class VendreActif(actifId: Int, quantite: Double, replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand

case class WrappedSuccess(replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand
case class WrappedFailure(replyTo: ActorRef[Confirmation], ex: Throwable) extends PortefeuilleCommand

sealed trait Confirmation
case object SuccessConfirmation extends Confirmation
case object FailureConfirmation extends Confirmation

object PortefeuilleActor {
  def apply(id: Int, utilisateurId: Int, nom: String, devise: String): Behavior[PortefeuilleCommand] =
    Behaviors.setup { context =>
      import PortefeuilleDAO._

      context.log.info(s"Démarrage du PortefeuilleActor pour $nom ($id) en $devise")

      def fetchValeurTotal(replyTo: ActorRef[BigDecimal]): Unit = {
        getValeurTotale(id).onComplete {
          case Success(value) =>
            context.log.info(s"Valeur totale du portefeuille $id : $value")
            replyTo ! value
          case Failure(ex) =>
            context.log.error(s"Erreur lors de la récupération de la valeur du portefeuille $id: ${ex.getMessage}")
            replyTo ! BigDecimal(0)
        }
      }

      def recalculerValeur(portefeuille: Portefeuille): Future[BigDecimal] = {
        PositionDAO.getByPortfolioId(portefeuille.id.get).flatMap { positions =>
          val futursPrix: Seq[Future[BigDecimal]] = positions.map(pos => ApiUtils.fetchStockData(pos.actif_id.toString))
          Future.sequence(futursPrix).map(_.sum)
        }
      }

      def updatedBehavior(portefeuille: Portefeuille): Behavior[PortefeuilleCommand] = {
        Behaviors.receiveMessage {
          case AjouterPosition(position, replyTo) =>
            context.log.info(s"Ajout de la position: $position")
            val nouveauPortefeuille = portefeuille.copy(valeurInitiale = portefeuille.valeurInitiale + position.prix_achat)
            update(nouveauPortefeuille).onComplete {
              case Success(_) => replyTo ! SuccessConfirmation
              case Failure(ex) =>
              context.pipeToSelf(update(nouveauPortefeuille)) {
                case Success(_) => WrappedSuccess(replyTo)
                case Failure(ex) => WrappedFailure(replyTo, ex)
              }
            }
            Behaviors.same

          case SupprimerPosition(actifId, replyTo) =>
            context.log.info(s"Suppression de la position avec actifId: $actifId")
            getByActifId(actifId).map(_.headOption).onComplete {
              case Success(Some(positionSup)) =>
                val nouvelleValeur = portefeuille.valeurInitiale - positionSup.prix_achat
                val nouveauPortefeuille = portefeuille.copy(valeurInitiale = nouvelleValeur)
                update(nouveauPortefeuille).onComplete {
                  case Success(_) => replyTo ! SuccessConfirmation
                  case Failure(ex) =>
                    context.log.error(s"Erreur lors de la suppression de la position: ${ex.getMessage}")
                    replyTo ! FailureConfirmation
                }
              case _ => replyTo ! FailureConfirmation
            }
            Behaviors.same

          case MiseAJourPrix(actifId, nouveauPrix) =>
            context.log.info(s"Mise à jour du prix de l'actif $actifId à $nouveauPrix")
            getByActifId(actifId).map(_.foreach { position =>
              val nouvelleValeur = position.quantite * nouveauPrix
              val nouveauPortefeuille = portefeuille.copy(valeurInitiale = nouvelleValeur)
              update(nouveauPortefeuille)
            })
            Behaviors.same

          case AcheterActif(actifId, quantite, prix, replyTo) =>
            context.log.info(s"Achat de l'actif $actifId en quantité $quantite à $prix")
            val nouvellePosition = Position(0, portefeuille.id.get, actifId, quantite, prix.toDouble, LocalDate.now())
            PositionDAO.insert(nouvellePosition).map { _ => replyTo ! SuccessConfirmation }
            Behaviors.same

          case VendreActif(actifId, quantite, replyTo) =>
            context.log.info(s"Vente de l'actif $actifId en quantité $quantite")
            PositionDAO.getByActifId(actifId).map(_.headOption).flatMap {
              case Some(position) if position.quantite >= quantite =>
                val nouvelleQuantite = position.quantite - quantite
                val updateFuture = if (nouvelleQuantite > 0) {
                  PositionDAO.update(position.id, nouvelleQuantite, position.prix_achat, position.date_achat)
                } else {
                  PositionDAO.delete(position.id)
                }
                updateFuture.map(_ => replyTo ! SuccessConfirmation)
              case _ =>
                Future.successful(replyTo ! FailureConfirmation)
            }
            Behaviors.same
          case WrappedSuccess(replyTo) =>
            replyTo ! SuccessConfirmation
            Behaviors.same

          case WrappedFailure(replyTo, ex) =>
            context.log.error(s"Erreur lors de l'ajout de la position: ${ex.getMessage}")
            replyTo ! FailureConfirmation
            Behaviors.same

          case ObtenirValeur(replyTo) =>
            context.log.info("Demande de la valeur totale du portefeuille")
            recalculerValeur(portefeuille).map(replyTo ! _)
            Behaviors.same
        }
      }

      getById(id).onComplete {
        case Success(Some(portefeuille)) =>
          context.log.info(s"Portefeuille $id chargé avec succès.")
          context.self ! ObtenirValeur(context.system.ignoreRef) // Charge la valeur au démarrage
        case _ =>
          context.log.error(s"Portefeuille $id non trouvé !")
      }

      updatedBehavior(Portefeuille(Some(id), utilisateurId, nom, devise))
    }
}
