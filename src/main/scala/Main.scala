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


import java.time.LocalDate

object Main extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val materializer = SystemMaterializer(system).materializer

 val emailTest = "herman@example5.com"

  // Appel de la fonction getByEmail
  val resultFuture = UserDAO.getByEmail(emailTest)

  // Attente du résultat (pour un test simple)
  val result = Await.result(resultFuture, Duration.Inf)

  // Affichage du résultat
  result match {
    case Nil => println(s"Aucun utilisateur trouvé avec l'email: $emailTest")
    case users => users.foreach(user => println(s"Utilisateur trouvé: ${user.nom}, Email: ${user.email}"))
  }

  // Arrêter l'application proprement
  System.exit(0)
}

