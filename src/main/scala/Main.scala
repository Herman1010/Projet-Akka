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

  //val insertResult3 = ActiveCoursesDAO.insert(newActiveCourse)
  //Await.result(insertResult3, Duration.Inf)

  println(s"Serveur demarre sur http://localhost:8080/\nAppuyez sur Entrer pour arreter...")
  StdIn.readLine()

  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}

