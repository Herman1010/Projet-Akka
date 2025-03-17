import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

sealed trait PortefeuilleCommand
case class AjouterPosition(position: Position, replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand
case class SupprimerPosition(actifId: Int, replyTo: ActorRef[Confirmation]) extends PortefeuilleCommand
case class ObtenirValeur(replyTo: ActorRef[BigDecimal]) extends PortefeuilleCommand

sealed trait Confirmation
case object Success extends Confirmation
case object Failure extends Confirmation


object PortefeuilleActor {
  def apply(id: Int, utilisateurId: Int, nom: String, devise: String): Behavior[PortefeuilleCommand] =
    Behaviors.setup { context =>
      def updatedBehavior(portefeuille: Portefeuille): Behavior[PortefeuilleCommand] = {
        Behaviors.receiveMessage {
          case AjouterPosition(position, replyTo) =>
            val nouveauPortefeuille = portefeuille.copy(positions = portefeuille.positions :+ position)
            replyTo ! Success
            updatedBehavior(nouveauPortefeuille)

          case SupprimerPosition(actifId, replyTo) =>
            val nouveauPortefeuille = portefeuille.copy(positions = portefeuille.positions.filterNot(_.actif.id == actifId))
            replyTo ! Success
            updatedBehavior(nouveauPortefeuille)

          case ObtenirValeur(replyTo) =>
            replyTo ! portefeuille.valeurTotale
            Behaviors.same
        }
      }

      updatedBehavior(Portefeuille(id, utilisateurId, nom, devise))
    }
}

