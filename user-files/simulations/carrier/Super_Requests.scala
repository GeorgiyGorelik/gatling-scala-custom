package carrier

import carrier.Parameters.{BasePath, protocolType, serverName, httpPort}
import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.http.Predef._
import scala.util.Random

object Super_Requests {

  var randomValue = 0

  var maxValue = ""
  var maxOrder = ""

  def generateRandomNumber(min: Int, max: Int): Int = {
    require(min <= max, "Invalid range: min should be less than or equal to max")
    val random = new Random()
    random.nextInt((max - min) + 1) + min
  }

  def R_01_Homepage = exec(http("R_01_Homepage")
    .get("/")
    .check(regex("""<input name="authenticity_token" type="hidden" value="(.*?)" />""").saveAs("authenticityToken"))
    .check(regex("""name="challenger\[step_id\]" type="hidden" value="(.*?)" />""").saveAs("challengerStepId"))
    .header("Sec-Fetch-Site","none"))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)
//    .exec(session => {
//      val extractedValue = session("authenticityToken").as[String]
//      val challengerStepId = session("challengerStepId").as[String]
//      println("!!!-----------!!!")
//      println(s"Extracted value: $extractedValue")
//      println(s"Extracted value: $challengerStepId")
//      session
//    })

  def R_02_Start = exec(http("R_02_Start")
    .post("/start")
    .queryParam("utf8", "✓")
    .queryParam("authenticity_token", session => {session("authenticityToken").as[String]})
    .queryParam("challenger[step_id]", session => {session("challengerStepId").as[String]})
    .queryParam("challenger[step_number]", "1")
    .queryParam("commit", "Start")
    .header("Referer", s"$protocolType://$serverName/")
    .header("Sec-Fetch-Site", "same-origin")
    .header("Origin", s"$protocolType://$serverName")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .check(status.is(302)))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)

  def R_02_Step = exec(http("R_02_Step")
    .get("/step/2")
    .check(regex("""name="challenger\[step_id\]" type="hidden" value="(.*?)" />""").saveAs("challengerStep2"))
    .header("Referer", s"$protocolType://$serverName/")
    .header("Sec-Fetch-Site", "same-origin"))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)
    .exec(session => {
      randomValue = generateRandomNumber(18, 100)
      session
    })

  def R_03_Start = exec(http("R_03_Start")
    .post("/start")
      .queryParam("utf8", "✓")
      .queryParam("authenticity_token", session => {session("authenticityToken").as[String]})
      .queryParam("challenger[step_id]", session => {session("challengerStep2").as[String]})
      .queryParam("challenger[step_number]", "2")
      .queryParam("challenger[age]", s"$randomValue")
      .queryParam("commit", "Start")
    .header("Referer", s"$protocolType://$serverName/step/2")
    //.header("Sec-Fetch-Site", "same-origin")
    .header("Origin", s"$protocolType://$serverName")
    .header("Content-Type", "application/x-www-form-urlencoded")
    .check(status.is(302)))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)

  def R_03_Step = exec(http("R_03_Step")
    .get("/step/3")
    .check(regex("""name="challenger\[step_id\]" type="hidden" value="(.*?)" />""").saveAs("challengerStep3"))
    .check(regex("""name="challenger\[order_selected\]" type="radio" value="(.*?)" />""").findAll.saveAs("orderSelected"))
    .check(regex(session => """">(\d+)</label></span></div>""").findAll.saveAs("allNumbers"))
    .header("Referer", s"$protocolType://$serverName/step/2")
    .header("Sec-Fetch-Site", "same-origin"))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)
    .exec(session => {
      val allValues = session("allNumbers").as[Seq[String]]
      val orderValues = session("orderSelected").as[Seq[String]]

      if (allValues.nonEmpty) {
        val numericValues = allValues.map(_.toDouble)
        val maxValues = numericValues.max
        val combinedValues: Seq[(String, String)] = allValues.zip(orderValues)

        for ((value, orderValue) <- combinedValues) {
          println(s"allValues: $value, orderValues: $orderValue")
          if (value.toDouble == maxValues) {
            maxValue = value
            maxOrder = orderValue
            println(s"this is it: $value, orderValues: $orderValue")
          }
        }
      }
      session
    })

  def R_04_Start = exec(http("R_04_Start")
    .post("/start")
    .queryParam("utf8", "✓")
    .queryParam("authenticity_token", session => {session("authenticityToken").as[String]})
    .queryParam("challenger[step_id]", session => {session("challengerStep3").as[String]})
    .queryParam("challenger[step_number]", "3")
    .queryParam("challenger[largest_order]", s"$maxValue")
    .queryParam("challenger[order_selected]", s"$maxOrder")
    .header("Referer", s"$protocolType://$serverName/step/3")
    .header("Origin", s"$protocolType://$serverName")
    .check(status.is(302))
  ).pause(Parameters.minThinkTime, Parameters.maxThinkTime)

  def R_04_Step = exec(http("R_04_Step")
    .get("/step/4")
    .check(regex("""name="challenger\[step_id\]" type="hidden" value="(.*?)" />""").saveAs("challengerStep4"))
    .header("Sec-Fetch-Site", "same-origin"))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)

  def R_05_Start = exec(http("R_05_Start")
    .post("/start")
    .queryParam("utf8", "✓")
    .queryParam("authenticity_token", session => {session("authenticityToken").as[String]})
    .queryParam("challenger[step_id]", session => {session("challengerStep4").as[String]})
    .queryParam("challenger[step_number]", "2")
    .queryParam("challenger[age]", s"$randomValue")
    //.queryParam("challenger[age]", s"${randomValue}")
    .queryParam("commit", "Start")
   .header("Referer", s"$protocolType://$serverName/step/2")
    .header("Origin", s"$protocolType://$serverName")
    .header("Content-Type", "application/x-www-form-urlencoded"))
    .pause(Parameters.minThinkTime, Parameters.maxThinkTime)
}