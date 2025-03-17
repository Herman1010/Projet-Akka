import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
case class ActiveCourses(id: Int, actif_id: Int, prix: Double, volume: Double, timestamp: Option[LocalDateTime] = Some(LocalDateTime.now()))

class ActiveCoursesTable(tag: Tag) extends Table[ActiveCourses](tag,"cours_actif"){
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def actif_id = column[Int]("actif_id")
  def prix = column[Double]("prix")
  def volume = column[Double]("volume")
  def timestamp = column[LocalDateTime]("timestamp",O.Default(LocalDateTime.now()))
  def * : ProvenShape[ActiveCourses] = (id,actif_id,prix,volume,timestamp.?) <> (ActiveCourses.tupled, ActiveCourses.unapply)
}

object ActiveCoursesDAO{
  val activeCourses = TableQuery[ActiveCoursesTable]
  val db = DatabaseConfig.db

  def insert(activeCourse: ActiveCourses): Future[Int] = db.run(activeCourses += activeCourse)

  def getAll(): Future[Seq[ActiveCourses]] = db.run(activeCourses.result)
}
