package carrier

import io.gatling.core.Predef.configuration
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.language.postfixOps

object Parameters {

  var users: Int = Integer.getInteger("users", 1)
  var rampUp: Integer = Integer.getInteger("rampUp", 1)
  var duration: Integer = Integer.getInteger("duration", 1)
  var rps: Integer = Integer.getInteger("rps", 1)

  val isDebug = true // false = real test execution
  if (isDebug) {
    users = 1
    rampUp = 1
    duration = 60
    rps = 5
  }

  // ___________________ Test Data ___________________
  //val testDataDir = s"C:\\Users\\Georgiy_Gorelik\\Desktop\\Gatling_Ramp-Up\\src\\test\\resources\\data-sets" // localhost DB
  // ____________________ Gateway ____________________
  val protocolType = "https"
  val serverName = "training.flooded.io"
  val httpPort = ""
  val defaultPath = "/"
  // _______________ Requests Defaults _______________
  val BaseURL = s"${protocolType}://${serverName}:${httpPort}/" // Directly to URL
  val BasePath = s"${defaultPath}" // Base request path

  def minThinkTime: FiniteDuration = 200 milliseconds
  def maxThinkTime: FiniteDuration = 800 milliseconds

  // ___________________ Headers ____________________
  val Main_Headers: Map[String, String] = Map(
    //"Sec-Fetch-Mode"            -> "navigate",
    //"Accept-Language"           -> "en-US,en;q=0.9",
    //"Upgrade-Insecure-Requests" -> "1",
    //"Sec-Fetch-User"            -> "?1",
   // "Accept-Encoding"           -> "gzip, deflate, br",
    //"User-Agent"                -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/117.0",
    //"Accept"                    -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
    //"Sec-Fetch-Dest"            -> "document"
  )

  // ________________ HTTP Protocols ________________
  val httpProtocolMain: HttpProtocolBuilder = http
    .baseUrl(BaseURL) // Here is the root for all relative URLs
    .headers(Main_Headers)
    .disableFollowRedirect
    .shareConnections // to simulate server to server traffic where the VU has a long-lived connection pool
}