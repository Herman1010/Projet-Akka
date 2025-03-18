import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import spray.json._
import akka.http.scaladsl.marshalling.ToResponseMarshallable


object Routes extends JsonFormats {
 /* val route: Route =
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
        }*/

    val routes = cors() {
  pathPrefix("api" / "message2") {
    get {
      onSuccess(UserDAO.getAll()){
                    utilisateur => complete(utilisateur.toJson)
    } ~
    delete {
      //parameter("id".as[Int]) { id =>
        //complete(deleteClient(id))
        complete(Message("Hello, Akka with JSON!"))
      }
    }
  }
}
}



