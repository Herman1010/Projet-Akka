import slick.jdbc.JdbcBackend.Database
import com.typesafe.config.ConfigFactory

object DatabaseConfig {
  private val config = ConfigFactory.load()
  val db = Database.forConfig("slick.db", config)

  def close(): Unit = db.close() // Permet de fermer la connexion proprement
}
