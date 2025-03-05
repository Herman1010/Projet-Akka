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

object Main extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val materializer = SystemMaterializer(system).materializer

  // Définition des routes
  val route = Routes.route
   
  // Démarrage du serveur HTTP
  val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8080).bind(route)

  val newUser = Utilisateur(4, "Herman", "herman@example3.com", "1234")

    val insertResult = UserDAO.insert(newUser)
    Await.result(insertResult, Duration.Inf)

    println("Utilisateur insere avec succes !")

  println(s"Serveur demarre sur http://localhost:8080/\nAppuyez sur Entrer pour arreter...")
  StdIn.readLine()

  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}

