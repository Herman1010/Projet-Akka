import slick.jdbc.PostgresProfile.api._
import java.time.LocalDateTime

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global




case class Position(id: Option[Int], portefeuille_id: Int, actif_id: Int,quantite: Double ,prix_achat:Double, date_achat: LocalDateTime)
  class PositionTable(tag: Tag) extends Table[Position](tag, "position") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def portefeuille_id = column[Int]("portefeuille_id")
  def actif_id = column[Int]("actif_id")
  def quantite = column[Double]("quantite")
  def prix_achat = column[Double]("prix_achat")
  def date_achat =column[LocalDateTime]("date_achat")
  def * = (id.?, portefeuille_id, actif_id, quantite, prix_achat, date_achat) <> (Position.tupled, Position.unapply)
}


object PositionDAO {
  val positions = TableQuery[PositionTable]
  val db = DatabaseConfig.db

  
  def insert(position: Position): Future[Int] = {
    val insertAction = positions += position
    db.run(insertAction)
  }
  def insertFront(position: Position): Future[Position] = {
    val insertAction = (positions returning positions.map(_.id.?) into ((pos, id) => pos.copy(id = id))) += position
    db.run(insertAction)
  }

  
  def getByPortfolioId(portefeuille_id: Int): Future[Seq[Position]] = {
    val query = positions.filter(_.portefeuille_id === portefeuille_id).result
    db.run(query)
  }

  

  def getByActifId(actif_id: Int): Future[Seq[Position]] = {
    val query = positions.filter(_.actif_id === actif_id).result
    db.run(query)
  }


  def getById(positionId: Int): Future[Option[Position]] = {
    val query = positions.filter(_.id === positionId).result.headOption
    db.run(query)
  }


  def delete(positionId: Int): Future[Int] = {
    val deleteAction = positions.filter(_.id === positionId).delete
    db.run(deleteAction)
  }

  
 
  def update(id: Int, quantite: Double, prix_achat: Double, date_achat: LocalDateTime): Future[Int] = {
    val updateAction = positions.filter(_.id === id)
      .map(p => (p.quantite, p.prix_achat, p.date_achat))
      .update((quantite, prix_achat, date_achat))
    db.run(updateAction) 
  }

  def getAll(): Future[Seq[Position]] = db.run(positions.result)

}
