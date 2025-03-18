import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate
import scala.concurrent.Future

object TypeTransaction extends Enumeration {
  type TypeTransaction = Value
  val Achat, Vente, Location = Value
}

case class Transaction(id: Int, portefeuille: Int, actif_id: Int, type_transaction: TypeTransaction.Value, quantite: Double, prix_unitaire: Double, date_transaction: LocalDate)

class TransactionsTable(tag: Tag) extends Table[Transaction](tag, "transaction") {

  implicit val typeTransactionMapper = MappedColumnType.base[TypeTransaction.Value, String](
    _.toString,
    str => TypeTransaction.values.find(_.toString == str).getOrElse(
      throw new IllegalArgumentException(s"Invalid TypeTransaction: $str")
    )
  )

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def portefeuille_id = column[Int]("portefeuille_id")
  def actif_id = column[Int]("actif_id")
  def type_transaction = column[TypeTransaction.Value]("type_transaction")
  def quantite = column[Double]("quantite")
  def prix_unitaire = column[Double]("prix_unitaire")
  def date_transaction = column[LocalDate]("date_transaction")
  def * = (id, portefeuille_id, actif_id, type_transaction, quantite, prix_unitaire, date_transaction) <> (Transaction.tupled, Transaction.unapply)
}

object TransactionDAO {
  val transactions = TableQuery[TransactionsTable]
  val db = DatabaseConfig.db

  def insert(transaction: Transaction): Future[Int] = {
    db.run((transactions returning transactions.map(_.id)) += transaction)
  }

  def getAll(): Future[Seq[Transaction]] = db.run(transactions.result)
}
