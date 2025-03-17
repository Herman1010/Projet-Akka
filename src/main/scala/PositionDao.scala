import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Future, ExecutionContext}

// Modèle de l'entité Position
case class Position(
    id: Option[Int] = None,
    portefeuilleId: Int,
    actifId: Int,
    quantite: BigDecimal,
    prixAchat: BigDecimal,
    dateAchat: Option[String] = None
)

// Mapping Slick pour la table Position
class PositionTable(tag: Tag) extends Table[Position](tag, "Position") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def portefeuilleId = column[Int]("portefeuille_id", O.NotNull)
  def actifId = column[Int]("actif_id", O.NotNull)
  def quantite = column[BigDecimal]("quantite", O.NotNull)
  def prixAchat = column[BigDecimal]("prix_achat", O.NotNull)
  def dateAchat = column[Option[String]]("date_achat", O.Default(None))

  // Clés étrangères vers Portefeuille et Actif
  def portefeuille = foreignKey("fk_position_portefeuille", portefeuilleId, TableQuery[PortefeuilleTable])(_.id, onDelete = ForeignKeyAction.Cascade)
  def actif = foreignKey("fk_position_actif", actifId, TableQuery[ActifTable])(_.id, onDelete = ForeignKeyAction.Cascade)

  def * = (id.?, portefeuilleId, actifId, quantite, prixAchat, dateAchat) <> (Position.tupled, Position.unapply)
}

// DAO pour gérer les opérations sur la table Position
class PositionDAO(db: Database)(implicit ec: ExecutionContext) {
  val positions = TableQuery[PositionTable]

  // Insérer une position
  def insert(position: Position): Future[Int] = {
    db.run(positions += position)
  }

  // Récupérer une position par son ID
  def findById(id: Int): Future[Option[Position]] = {
    db.run(positions.filter(_.id === id).result.headOption)
  }

  // Récupérer toutes les positions d'un portefeuille donné
  def findByPortefeuille(portefeuilleId: Int): Future[Seq[Position]] = {
    db.run(positions.filter(_.portefeuilleId === portefeuilleId).result)
  }

  // Mettre à jour une position
  def update(id: Int, position: Position): Future[Int] = {
    db.run(positions.filter(_.id === id).update(position.copy(id = Some(id))))
  }

  // Supprimer une position
  def delete(id: Int): Future[Int] = {
    db.run(positions.filter(_.id === id).delete)
  }
}
