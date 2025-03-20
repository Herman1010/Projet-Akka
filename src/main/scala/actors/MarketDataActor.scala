  import PositionDAO._
  import ApiUtils._
  import akka.actor.typed.{ActorRef, Behavior}
  import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

  import scala.util.{Failure, Success}
  import scala.concurrent.ExecutionContext.Implicits.global

  sealed trait MarketDataEvent
  case class PrixActifMisAJour(actifId: Int, prix: BigDecimal) extends MarketDataEvent

  object MarketDataActor {
    def apply(): Behavior[MarketDataEvent] =
      Behaviors.setup { context =>
        Behaviors.receiveMessage {
          case PrixActifMisAJour(actifId, prix) =>
            context.log.info(s"Prix mis à jour pour actif $actifId : $prix")
            Behaviors.same
        }
      }
  }

