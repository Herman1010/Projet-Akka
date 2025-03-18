import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

final case class Message(message: String)

trait JsonFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object LocalDateTimeFormat extends JsonFormat[LocalDateTime] {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    def write(ldt: LocalDateTime): JsValue = JsString(ldt.format(formatter))
    def read(json: JsValue): LocalDateTime = json match {
      case JsString(str) => LocalDateTime.parse(str, formatter)
      case _ => throw DeserializationException("LocalDateTime expected")
    }
  }

  implicit val messageFormat: RootJsonFormat[Message] = jsonFormat1(Message)
  implicit val userFormat = jsonFormat4(Utilisateur) 
  implicit val notifFormat = jsonFormat5(Notifications)
  implicit val ActiveCourseFormat = jsonFormat5(ActiveCourses)
  implicit val ActifFormat: RootJsonFormat[Actif] = jsonFormat6(Actif)


}
