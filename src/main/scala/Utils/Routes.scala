import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import spray.json._
import akka.http.scaladsl.marshalling.ToResponseMarshallable

object Routes extends JsonFormats {
  //val route: Route =
  val routes = cors() {
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
          //complete(Message(UserDAO.getAll()))
          onSuccess(PortefeuilleDAO.getAll()) {
            portefeuille => complete(portefeuille.toJson)
          }
        }
      }~
      pathPrefix("api" / "actifs") {
        get {
          //complete(Message(UserDAO.getAll()))
          onSuccess(ActifDAO.getAll()) {
            actif => complete(actif.toJson)
          }
        }
      }
  }
}



