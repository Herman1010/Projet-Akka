import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
case class Notifications(id: Int, utilisateur_id: Int, message: String, lu: Boolean =false, date_Envoi: Option[LocalDateTime] = Some(LocalDateTime.now()))

class NotifTable(tag: Tag) extends Table[Notifications](tag, "notification") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def utilisateur_id = column[Int]("utilisateur_id")
  def message = column[String]("message")
  def lu = column[Boolean]("lu",O.Default(false))
  def date_envoi = column[LocalDateTime]("date_envoi",O.Default(LocalDateTime.now()))
  def * : ProvenShape[Notifications] = (id, utilisateur_id, message, lu, date_envoi.?) <> (Notifications.tupled, Notifications.unapply)
}

object NotifDAO {
  val notifications = TableQuery[NotifTable]
  val db = DatabaseConfig.db
  
  def insert(notif: Notifications): Future[Int] = db.run(notifications += notif)

  def getAll(): Future[Seq[Notifications]] = db.run(notifications.result)
}