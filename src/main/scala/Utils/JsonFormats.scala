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
  implicit val PortefeuilleFormat: RootJsonFormat[Portefeuille] = jsonFormat5(Portefeuille)
  //implicit val PositionFormat = jsonFormat6(Position)
  import java.time.LocalDateTime
  import java.time.format.DateTimeFormatter

  implicit val positionFormat: RootJsonFormat[Position] = new RootJsonFormat[Position] {
    val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    def write(p: Position): JsValue = JsObject(
      "id" -> p.id.toJson,
      "portefeuille_id" -> JsNumber(p.portefeuille_id),
      "actif_id" -> JsNumber(p.actif_id),
      "quantite" -> JsNumber(p.quantite),
      "prix_achat" -> JsNumber(p.prix_achat),
      "date_achat" -> JsString(p.date_achat.format(formatter))
    )

    def read(json: JsValue): Position = {
      val fields = json.asJsObject.fields

      Position(
        id = fields.get("id").flatMap(_.convertTo[Option[Int]]),
        portefeuille_id = fields("portefeuille_id").convertTo[Int],
        actif_id = fields("actif_id").convertTo[Int],
        quantite = fields("quantite").convertTo[Double],
        prix_achat = fields("prix_achat").convertTo[Double],
        date_achat = LocalDateTime.parse(fields("date_achat").convertTo[String], formatter)
      )
    }
  }


}
