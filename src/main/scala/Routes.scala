import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json._
import akka.http.scaladsl.marshalling.ToResponseMarshallable


object Routes extends JsonFormats {
  val route: Route =
    path("api" / "message") {
      get {
        complete(Message("Hello, Akka with JSON!"))
      }
    } ~
    path("api"/"message2"){
            get {
                //complete(Message(UserDAO.getAll()))
                onSuccess(UserDAO.getAll()){
                    utilisateur => complete(utilisateur.toJson)
                }
            }
        } ~
      path("api"/ "notif"){
        get {
          onSuccess(NotifDAO.getAll()){
            notifications => complete(notifications.toJson)
          }
        }
      } ~
      path("api" / "ActiveCourses"){
        get {
          onSuccess(ActiveCoursesDAO.getAll()){
            activeCourses => complete(activeCourses.toJson)
          }
        }
      }

    
}
