import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import spray.json._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.HttpMethods.{DELETE, PUT,POST,GET,OPTIONS}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.Future
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import ch.megard.akka.http.cors.scaladsl.settings._
import ch.megard.akka.http.cors.scaladsl.model.HttpOriginMatcher

object Routes extends JsonFormats {

  val corsSettings = CorsSettings.defaultSettings.withAllowCredentials(true)
    .withAllowedMethods(Seq(GET,POST,PUT,DELETE,OPTIONS))
    .withAllowedOrigins(HttpOriginMatcher.*)
  //val route: Route =
  val routes = cors(corsSettings) {
    pathPrefix("api" / "message2") {
      get {
        onSuccess(UserDAO.getAll()) {
          utilisateur => complete(utilisateur.toJson)
        } ~
          delete {
            //parameter("id".as[Int]) { id =>
            //complete(deleteClient(id))
            complete(Message("Hello, Akka with JSON!"))
          }
      }
    } ~
      pathPrefix("api" / "message") {
        get {
          complete(Message("Hello, Akka with JSON!"))
        }
      } ~
      path("api" / "notif") {
        get {
          onSuccess(NotifDAO.getAll()) {
            notifications => complete(notifications.toJson)
          }
        }
      } ~
      pathPrefix("api" / "ActiveCourses") {
        get {
          onSuccess(ActiveCoursesDAO.getAll()) {
            activeCourses => complete(activeCourses.toJson)
          }
        }
      }~
      pathPrefix("api" / "portefeuilles") {
        get {
          onSuccess(PortefeuilleDAO.getAll()) {
            portefeuille => complete(portefeuille.toJson)
          }
        } ~
          post {
            entity(as[String]) { body =>
              val json = body.parseJson.convertTo[Portefeuille]
              onSuccess(PortefeuilleDAO.insert(json)) { result =>
                complete(StatusCodes.Created, result.toJson)
              }
            }
          } ~
          delete {
            path(IntNumber) { id =>
              onSuccess(PortefeuilleDAO.delete(id)) { result =>
                complete(StatusCodes.OK, s"Position $id supprimée")
              }
            }
          }
      }~
      pathPrefix("api" / "actifs") {
        get {
          onSuccess(ActifDAO.getAll()) {
            actif => complete(actif.toJson)
          }
        }
      }~
      pathPrefix("api" / "position") {
        get {
          onSuccess(PositionDAO.getAll()) { positions =>
            complete(positions.toJson)
          }
        } ~
          post {
            entity(as[String]) { body =>
              val json = body.parseJson.convertTo[Position]
              onSuccess(PositionDAO.insertFront(json)) { result =>
                complete(StatusCodes.Created, result.toJson)
              }
            }
          } ~
          delete {
            path(IntNumber) { id =>
              onSuccess(PositionDAO.delete(Some(id))) { result =>
                complete(StatusCodes.OK, s"Position $id supprimée")
              }
            }
          }
      }
  }
}



