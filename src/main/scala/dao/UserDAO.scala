import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Utilisateur(id: Option[Int] = None, nom: String, email: String, mot_de_passe_hash: String)

class UsersTable(tag: Tag) extends Table[Utilisateur](tag, "utilisateur") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def nom = column[String]("nom")
  def email = column[String]("email")
  def mot_de_passe_hash = column[String]("mot_de_passe_hash")
  def * = (id.?, nom, email,mot_de_passe_hash) <> (Utilisateur.tupled, Utilisateur.unapply)
}

object UserDAO {
  val utilisateur = TableQuery[UsersTable]
  val db = DatabaseConfig.db

  def insert(user: Utilisateur): Future[Int] = db.run(utilisateur returning utilisateur.map(_.id) += user)

  def getAll(): Future[Seq[Utilisateur]] = db.run(utilisateur.result)
  def getByEmail(email: String) : Future[Seq[Utilisateur]] = db.run(utilisateur.filter(_.email === email).result)
}
