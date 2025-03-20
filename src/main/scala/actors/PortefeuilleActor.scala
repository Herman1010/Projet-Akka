import PositionDAO._
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.LocalDateTime

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

      def recalculerValeur(portefeuille: Portefeuille): Future[BigDecimal] = {
        PositionDAO.getByPortfolioId(portefeuille.id.get).flatMap { positions =>
          val futursPrix: Seq[Future[BigDecimal]] = positions.map(pos => ApiUtils.fetchStockData(pos.actif_id.toString))
          Future.sequence(futursPrix).map(_.sum)
        }
      }

      def updatedBehavior(portefeuille: Portefeuille): Behavior[PortefeuilleCommand] = {
        Behaviors.receiveMessage {
          case AjouterPosition(position, replyTo) =>
            PositionDAO.insert(position).onComplete {
              case Success(_) => replyTo ! SuccessConfirmation
              case Failure(_) => replyTo ! FailureConfirmation
            }
            Behaviors.same

          case SupprimerPosition(actifId, replyTo) =>
            PositionDAO.getByActifId(actifId).map(_.headOption).onComplete {
              case Success(Some(position)) =>
                PositionDAO.delete(position.id).onComplete {
                  case Success(_) => replyTo ! SuccessConfirmation
                  case Failure(_) => replyTo ! FailureConfirmation
                }
              case _ => replyTo ! FailureConfirmation
            }
            Behaviors.same

          case MiseAJourPrix(actifId, nouveauPrix) =>
            PositionDAO.getByActifId(actifId).map(_.foreach { position =>
              PositionDAO.update(position.id, position.quantite, nouveauPrix.toDouble)
            })
            Behaviors.same

          case AcheterActif(actifId, quantite, prix, replyTo) =>
            val nouvellePosition = Position(0, portefeuille.id.get, actifId, quantite, prix.toDouble, Some(LocalDateTime.now()))
            PositionDAO.insert(nouvellePosition).map { _ => replyTo ! SuccessConfirmation }
            Behaviors.same

          case VendreActif(actifId, quantite, replyTo) =>
            PositionDAO.getByActifId(actifId).map(_.headOption).flatMap {
              case Some(position) if position.quantite >= quantite =>
                val nouvelleQuantite = position.quantite - quantite
                val updateFuture = if (nouvelleQuantite > 0) {
                  PositionDAO.update(position.id, nouvelleQuantite, position.prix_achat)
                } else {
                  PositionDAO.delete(position.id)
                }
                updateFuture.map(_ => replyTo ! SuccessConfirmation)
              case _ =>
                Future.successful(replyTo ! FailureConfirmation)
            }
            Behaviors.same

          case ObtenirValeur(replyTo) =>
            recalculerValeur(portefeuille).map(replyTo ! _)
            Behaviors.same
        }
      }

      getById(id).onComplete {
        case Success(Some(portefeuille)) =>
          context.log.info(s"Portefeuille $id chargé avec succès.")
        case _ =>
          context.log.error(s"Portefeuille $id non trouvé !")
      }
      updatedBehavior(Portefeuille(Some(id), utilisateurId, nom, devise))
    }
}