import sttp.client3._
import io.circe.parser._
import scala.concurrent.{Future, ExecutionContext}

object ApiUtils {
  private val apiKey = "XSCR7AABADWCKNQC"

  // Ajout de l'implicit ExecutionContext
  implicit val ec: ExecutionContext = ExecutionContext.global

  def fetchStockData(symbol: String): Future[BigDecimal] = {
    val newValue = {
    val url = s"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=$symbol&interval=1min&apikey=$apiKey"
    val backend = HttpURLConnectionBackend()
    val response = basicRequest.get(uri"$url").send(backend)
    println(s"Réponse brute API : ${response.body}")
    response.body match {
      case Right(jsonStr) =>
        parse(jsonStr).toOption
          .flatMap(_.hcursor.downField("Time Series (1min)").values.flatMap(_.headOption))
          .flatMap(_.hcursor.downField("4. close").as[String].toOption)
          .map(BigDecimal.apply)
          .getOrElse(BigDecimal(0))

      case Left(_) => BigDecimal(0)
    }
    
  }
    Future (newValue)
  }
}
