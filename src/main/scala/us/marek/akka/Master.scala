package us.marek.akka

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{ActorRefRoutee, Router, RoundRobinRoutingLogic}

class Master(routeeCount: Int, resultCount: Int, responseListener: ActorRef,
             benchmarkListener: ActorRef, printResponses: Boolean,
             nthResponse: Int) extends Actor {
  
  var currentNthResponse = 0
  var numRequests = 0
  var numResults = 0
  var startTime: Long = 0
  
  val router = {
    val routees = Vector.fill(routeeCount) {
      val r = context actorOf Props[RserveActor] 
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }
  
  def receive = {
    
    case msg: RRequest => {
      numRequests += 1
      if (numRequests == 1)  {
        startTime = System currentTimeMillis
      }
      router.route(msg, self)
    }
    
    case msg: RResponse => {
      numResults += 1
      currentNthResponse += 1
      if (printResponses && currentNthResponse % nthResponse == 0) {
        responseListener ! msg.toString
        currentNthResponse = 0
      }
      if (numResults == resultCount) {
        val endTime = System currentTimeMillis
        val latency = 1.0 * ( endTime - startTime) / resultCount
        benchmarkListener ! LatencyThroughput(latency, numRequests)
      }
    }
    
    case Terminate => context.system.shutdown()
    
    case _ => 
  }
  
}