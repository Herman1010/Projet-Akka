
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global

// modele
case class Portefeuille(
  id: Option[Int] = None,
  utilisateurId: Int,
  nom: String,
  devise: String,
  valeurInitiale: BigDecimal = 0
)

class PortefeuilleTable(tag: Tag) extends Table[Portefeuille](tag, "portefeuille") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def utilisateurId = column[Int]("utilisateur_id")
  def nom = column[String]("nom", O.Length(100))
  def devise = column[String]("devise")
  def valeurInitiale = column[BigDecimal]("valeur_initiale", O.Default(BigDecimal(0)))

  def utilisateurFk = foreignKey("portefeuille_utilisateur_id_fkey", utilisateurId, TableQuery[UsersTable])(
    _.id, ForeignKeyAction.Cascade, ForeignKeyAction.NoAction
  )

  def * = (id.?, utilisateurId, nom, devise, valeurInitiale) <> (Portefeuille.tupled, Portefeuille.unapply)
}

// dao

object PortefeuilleDAO {
  private val portefeuilleQuery = TableQuery[PortefeuilleTable]
  private val db = DatabaseConfig.db

  def insert(portefeuille: Portefeuille): Future[Int] =
    db.run(portefeuilleQuery returning portefeuilleQuery.map(_.id) += portefeuille)

  def getById(id: Int): Future[Option[Portefeuille]] =
    db.run(portefeuilleQuery.filter(_.id === id).result.headOption)

  def getByUtilisateur(utilisateurId: Int): Future[Seq[Portefeuille]] =
    db.run(portefeuilleQuery.filter(_.utilisateurId === utilisateurId).result)

  def getAll() : Future[Seq[Portefeuille]] = db.run(portefeuilleQuery.result)

  def delete(id: Int): Future[Int] =
    db.run(portefeuilleQuery.filter(_.id === id).delete)
}