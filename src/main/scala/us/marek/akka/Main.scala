package us.marek.akka

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import scala.util.Random

object Main extends App {
  
  val actorCount = 4
  val multiplier = 1000
  val printResponses = true
  val nthResponse = 1000
  val rand = new Random()
  
  val config = ConfigFactory.load()
  val system = ActorSystem.create("rServeActorSystem", config.getConfig("myapp1"))
  
  val benchmarkListener = system.actorOf(Props[BenchmarkListener], "benchmark")
  val responseListener = system.actorOf(Props[ResponseListener], "response")
  val master = system.actorOf(Props(classOf[Master], actorCount, actorCount * multiplier,
      responseListener, benchmarkListener, printResponses, nthResponse), "master")
  
  for (i <- 0 until actorCount * multiplier) master ! RRequest(s"predict(fit,kyphosis[${rand.nextInt(40) + 1},])")
  
  Thread.sleep(5000)
  
  master ! Terminate
  
}