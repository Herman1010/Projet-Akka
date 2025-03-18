import slick.jdbc.PostgresProfile.api._
import java.time.LocalDate
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global




  case class position(id: Int, portefeuille_id: Int, actif_id: Int,quantite: Double ,prix_achat:Double, date_achat: LocalDate)
  class Positions(tag: Tag) extends Table[Position](tag, "positions") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def portefeuille_id = column[Int]("portefeuille_id")
  def actif_id = column[Int]("actif_id")
  def quantite = column[Double]("quantite")
  def prix_achat = column[Double]("prix_achat")
  def date_achat =column[LocalDate]("date_achat")
  def * = (id, portefeuille_id, actif_id, quantite, prix_achat, date_achat) <> (Position.tupled, Position.unapply)
}

object Positions {
  val query = TableQuery[Positions]
}

object PositionDAO {
  val positions = TableQuery[Positions]
  val db = DatabaseConfig.db

  // Insertion d'une nouvelle position en prenant directement un objet Position
  def addPosition(position: Position): Future[Int] = {
    val insertAction = positions += position
    db.run(insertAction)
  }

  // Récupérer toutes les positions d'un portefeuille
  def getPositionsByPortfolio(portefeuille_id: Int): Future[Seq[Position]] = {
    val query = positions.filter(_.portefeuille_id === portefeuille_id).result
    db.run(query)
  }

  // Récupérer toutes les positions d'un actif
  def getPositionsByAsset(assetId: Int): Future[Seq[Position]] = {
    val query = positions.filter(_.actif_id === actif_id).result
    db.run(query)
  }

  // Récupérer une position par son ID
  def getPositionById(positionId: Int): Future[Option[Position]] = {
    val query = positions.filter(_.id === positionId).result.headOption
    db.run(query)
  }

  // Supprimer une position
  def removePosition(positionId: Int): Future[Int] = {
    val deleteAction = positions.filter(_.id === positionId).delete
    db.run(deleteAction)
  }

  // Modifier une position existante
  def updatePosition(id: Int, quantity: Double, purchasePrice: Double, purchaseDate: LocalDate): Future[Int] = {
    val updateAction = positions.filter(_.id === id)
      .map(p => (p.quantite, p.prix_achat, p.date_achat))
      .update((quantite, prix_achat, date_achat))
    db.run(updateAction)
  }
}
