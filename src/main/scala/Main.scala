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

import sttp.client3._
import io.circe.parser._
import io.circe.generic.auto._
import io.circe.Json

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
  //implicit val materializer = SystemMaterializer(system).materializer
  implicit val materializer: akka.stream.Materializer = SystemMaterializer(system).materializer


  // Définition des routes
  val route = Routes.routes
   
  // Démarrage du serveur HTTP
  val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8080).bind(route)

  //val newUser = Utilisateur(4, "Herman", "herman@example3.com", "1234")

    //val insertResult = UserDAO.insert(newUser)
   // Await.result(insertResult, Duration.Inf)

    //println("Utilisateur insere avec succes !")

   val apiKey = "XSCR7AABADWCKNQC"
  val symbol = "AAPL"  // Symbole boursier d'Apple

  // URL de l'API pour récupérer les données boursières d'une action
  val url = s"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=$symbol&interval=1min&apikey=$apiKey"
  //val url = s"https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=$symbol&apikey=$apiKey"


  // Envoi de la requête HTTP à l'API Alpha Vantage
  val backend = HttpURLConnectionBackend()
  val response = basicRequest.get(uri"$url").send(backend)

  // Parser la réponse JSON
  response.body match {
    case Right(json) =>
      // Parse et affiche les données (on les affiche ici en brut)
      println(s"Réponse JSON : $json")

      // Ici, tu peux utiliser circe pour parser le JSON de façon plus propre, par exemple
      // et extraire les informations spécifiques sur les prix
      val parsed = parse(json).getOrElse(Json.Null)
      println(parsed)

    case Left(error) =>
      println(s"Erreur : $error")
  }

/*
  val newUser = Utilisateur(5, "Herman", "herman@aaaa.com", "1234")

    val insertResult = UserDAO.insert(newUser)
    Await.result(insertResult, Duration.Inf)
  println("Utilisateur insere avec succes !")

  val newNotif = Notifications(10,1,"message test")

  val insertResult2 = NotifDAO.insert(newNotif)
  Await.result(insertResult2, Duration.Inf)
  println("Notif insere avec succes !")
  val portefeuille = Portefeuille(None, 5,"portefeuille1","EUR",1000)
  // Insert portfolio
  val ajoutResult = PortefeuilleDAO.insert(portefeuille)
  val PortefeuilleId = Await.result(ajoutResult, Duration.Inf)
  println(s"portefeuille ajouté avec ID: $PortefeuilleId")

  /* Retrieve the portfolio */
  val fetchedActif = Await.result(PortefeuilleDAO.getById(PortefeuilleId), Duration.Inf)
  println(s"portefeuille récupéré: $fetchedActif")

  // Delete the portfolio
  val suppressionResult = Await.result(PortefeuilleDAO.delete(PortefeuilleId), Duration.Inf)
  println(suppressionResult)


  //val newActiveCourse = ActiveCourses(1,1,10.57,2.0)
  println("Notif insere avec succes !")

  //val insertResult3 = ActiveCoursesDAO.insert(newActiveCourse)
  //Await.result(insertResult3, Duration.Inf)

  val newTransaction = Transaction(4, 2, 3, TypeTransaction.Achat, 12, 12, LocalDate.of(2024, 5, 10))

  val insertResult4 = TransactionDAO.insert(newTransaction)
  Await.result(insertResult4, Duration.Inf)

  println("Transaction insere avec succes !")

  /*val newActiveCourse = ActiveCourses(1,1,10.57,2.0)

  //val insertResult3 = ActiveCoursesDAO.insert(newActiveCourse)
  //Await.result(insertResult3, Duration.Inf)
  val insertResult3 = ActiveCoursesDAO.insert(newActiveCourse)
  Await.result(insertResult3, Duration.Inf)*/
*/
  println(s"Serveur demarre sur http://localhost:8080/\nAppuyez sur Entrer pour arreter...")
  StdIn.readLine()

  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}