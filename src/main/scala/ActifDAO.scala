import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import java.time.LocalDateTime
import slick.lifted.ProvenShape

// modele
case class Actif(
  id: Option[Int] = None,
  symbole: String,
  nom: String,
  typeActif: String,
  marche: Option[String],
  dateCreation: Option[LocalDateTime] = Some(LocalDateTime.now())
)


class ActifTable(tag: Tag) extends Table[Actif](tag, "actif") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def symbole = column[String]("symbole", O.Unique)
  def nom = column[String]("nom")
  def typeActif = column[String]("type")
  def marche = column[Option[String]]("marche")
  def dateCreation = column[LocalDateTime]("date_creation",O.Default(LocalDateTime.now()))
  
  def * : ProvenShape[Actif] = (id.?, symbole, nom, typeActif, marche, dateCreation.?) <> (Actif.tupled, Actif.unapply)
}

// dao
object ActifDAO {
  private val actifQuery = TableQuery[ActifTable]
  private val db = DatabaseConfig.db

  def insert(actif: Actif): Future[Int] = db.run(actifQuery returning actifQuery.map(_.id) += actif)

  def getAll(): Future[Seq[Actif]] = db.run(actifQuery.result)

  def getById(id: Int): Future[Option[Actif]] = db.run(actifQuery.filter(_.id === id).result.headOption)

  def getBySymbole(symbole: String): Future[Option[Actif]] = db.run(actifQuery.filter(_.symbole === symbole).result.headOption)

  def update(id: Int, actif: Actif): Future[Int] = 
    db.run(actifQuery.filter(_.id === id).update(actif.copy(id = Some(id))))

  def delete(id: Int): Future[Int] = db.run(actifQuery.filter(_.id === id).delete)
}



