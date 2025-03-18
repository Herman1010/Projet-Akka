import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.stream.SystemMaterializer

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem

import scala.concurrent.Await
import scala.concurrent.duration.Duration


import scala.io.StdIn
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Future

import sttp.client3._
import io.circe.parser._
import io.circe.generic.auto._
import io.circe.Json


object Main extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
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


  println(s"Serveur demarre sur http://localhost:8080/\nAppuyez sur Entrer pour arreter...")
  StdIn.readLine()

  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}

