package example

import io.gatling.core.Predef._

//import io.gatling.core.Predef.{Simulation, _}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class SuperSimulation extends Simulation {

  before {
    println("### Gatling simulation is about to start ###")
//    printf(s"\tTest parameters: " +
//      s"\n\tVirtual Users: %d, Ramp-Up: %d, Constant load: %d, Throughput: %d RPS, Debug mode: %b. ",
//      Parameters.users, Parameters.rampUp, Parameters.duration, Parameters.rps, Parameters.isDebug)
    println("************* Test start time: %d ms *************\n", System.currentTimeMillis())
  }

  setUp(
    example.Scenario.DefaultScenario.inject(atOnceUsers(1))
  ).protocols(Parameters.httpProtocolMain)

  after {
    println("######")
    printf("************* Test end time: %d ms *************\n", System.currentTimeMillis())
  }
}
