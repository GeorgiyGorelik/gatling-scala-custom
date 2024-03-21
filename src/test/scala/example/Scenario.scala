package example

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.language.postfixOps

object Scenario {

  var loopCount: Int = Integer.getInteger("LoopCount", 1).toInt

  def DefaultScenario: ScenarioBuilder = scenario("Ramp-Up")
    .repeat(loopCount) {
      exec(Super_Requests.R_01_Homepage)
      .exec(Super_Requests.R_02_Start, Super_Requests.R_02_Step)
      .exec(Super_Requests.R_03_Start, Super_Requests.R_03_Step)
      .exec(Super_Requests.R_04_Start, Super_Requests.R_04_Step)
    }
}