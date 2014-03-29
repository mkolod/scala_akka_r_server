package us.marek.akka

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{ActorRefRoutee, Router, RoundRobinRoutingLogic}

@edu.umd.cs.findbugs.annotations.SuppressWarnings
class Master(routeeCount: Int) extends Actor {
  
  val router = {
    val routees = Vector.fill(routeeCount) {
      val r = context.actorOf(Props[RserveActor] )
      context.watch(r)
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }
  
  def receive = {
    
    case msg: RRequest => router.route(msg, sender)
    
    case Terminate => context.system.shutdown()
    
    case _ => 
  }
  
}
