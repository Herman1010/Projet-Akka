import akka.actor.typed.{ActorSystem, ActorRef}
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import java.time.LocalDateTime

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.stream.SystemMaterializer

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import PortefeuilleActor._
import MarketDataActor._

object Main extends App {
  /*implicit val system: ActorSystem[PortefeuilleCommand] = ActorSystem(PortefeuilleActor(1, 1, "MonPortefeuille", "EUR"), "PortefeuilleSystem")
  import system.executionContext
  implicit val timeout: Timeout = 10.seconds

  val testActifId = 6
  val testPosition = Position(1, portefeuille_id = 1, actif_id = 6, quantite = 50.0, prix_achat = 100.5, date_achat = Some(LocalDateTime.now()))

   //Ajouter une position
  val ajoutFuture = system.ask(replyTo => AjouterPosition(testPosition, replyTo))
  println("Ajout Position: " + Await.result(ajoutFuture, timeout.duration))

  // Acheter un actif
  val achatFuture = system.ask(replyTo => AcheterActif(testActifId, 5.0, 120.0, replyTo))
  println("Achat Actif: " + Await.result(achatFuture, timeout.duration))

  // Obtenir la valeur du portefeuille
  val valeurFuture = system.ask(replyTo => ObtenirValeur(replyTo))
  println("Valeur Portefeuille: " + Await.result(valeurFuture, timeout.duration))

  // Mettre à jour le prix d'un actif
  system ! MiseAJourPrix(testActifId, 130.0)
  println("Mise à jour prix envoyée")

  // Vendre un actif
  val venteFuture = system.ask(replyTo => VendreActif(testActifId, 5.0, replyTo))
  println("Vente Actif: " + Await.result(venteFuture, timeout.duration))
*/



  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val materializer: akka.stream.Materializer = SystemMaterializer(system).materializer

    //Démarrage du serveur HTTP
    val route = Routes.route
    val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8080).bind(route)

    println("Serveur démarré sur http://localhost:8080")
    println("Appuyez sur Entrée pour arrêter...")

    // Tester la récupération des prix
    val futurePrix: Future[BigDecimal] = ApiUtils.fetchStockData("AAPL")

    futurePrix.onComplete {
      case Success(prix) => println(s"Prix récupéré : $prix")
      case Failure(ex)   => println(s"Erreur lors de la récupération du prix : ${ex.getMessage}")
    }

    // Attendre l'entrée de l'utilisateur pour arrêter le serveur proprement
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind()) // Libère le port
      .onComplete { _ =>
        println("Arrêt du système...")
        system.terminate()
      }
 
  
 // val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8080).bind(route)
/*  val system: ActorSystem[PortefeuilleCommand] = ActorSystem(PortefeuilleActor(1, 1001, "MonPortefeuille", "EUR"), "TestSystem")

    implicit val ec: ExecutionContext = system.executionContext

    // Création des acteurs
    val portefeuilleActor: ActorRef[PortefeuilleCommand] = system
    val marketDataActor: ActorRef[MarketDataEvent] = system.systemActorOf(MarketDataActor(), "MarketDataActor")

    // Test d'ajout de position
    val testPosition = Position(0, 1, 123, 10, 100.0, java.time.LocalDate.now())
    portefeuilleActor ! AjouterPosition(testPosition, system.ignoreRef)

    // Simulation de mise à jour des prix
    marketDataActor ! PrixActifMisAJour(123, 105.0)
    portefeuilleActor ! MiseAJourPrix(123, 105.0)

    // Récupération de la valeur totale du portefeuille
    portefeuilleActor ! ObtenirValeur(system.ignoreRef)

    // Correction : Utilisation d'un Runnable pour l'arrêt propre du système
    system.scheduler.scheduleOnce(3.seconds, new Runnable {
      override def run(): Unit = system.terminate()
    })
*/


/*
  implicit val timeout: Timeout = 3.seconds
  implicit val ec: ExecutionContext = ExecutionContext.global

  def main(args: Array[String]): Unit = {
    val system: ActorSystem[PortefeuilleCommand] =
      ActorSystem(PortefeuilleActor(1, 1001, "MonPortefeuille", "EUR"), "PortefeuilleSystem")

    // Fonction pour envoyer des messages et récupérer la réponse
    def sendCommand[T](command: ActorRef[T] => PortefeuilleCommand): T = {
      val future: Future[T] = system.ask[T](command)(timeout, system.scheduler)
      Await.result(future, timeout.duration)
    }

    // Ajouter une position
    val position = Position(1, 2, 3, 150.00, 1500, LocalDate.of(2024,12,12)) // Exemple de position Apple à 150€/action
    val ajoutResult = sendCommand[Confirmation](replyTo => AjouterPosition(position, replyTo))
    println(s"Ajout de position: $ajoutResult")

    // Obtenir la valeur totale du portefeuille
    val valeurPortefeuille = sendCommand[BigDecimal](replyTo => ObtenirValeur(replyTo))
    println(s"Valeur totale du portefeuille: $valeurPortefeuille EUR")

    // Supprimer une position
    val suppressionResult = sendCommand[Confirmation](replyTo => SupprimerPosition(1, replyTo))
    println(s"Suppression de position: $suppressionResult")

    // Vérifier la valeur après suppression
    val nouvelleValeurPortefeuille = sendCommand[BigDecimal](replyTo => ObtenirValeur(replyTo))
    println(s"Nouvelle valeur du portefeuille: $nouvelleValeurPortefeuille EUR")

    
  }
*/
    //println("Appuyez sur Entrée pour arrêter...")
    //StdIn.readLine() // Attend que l'utilisateur appuie sur Entrée

    //bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}