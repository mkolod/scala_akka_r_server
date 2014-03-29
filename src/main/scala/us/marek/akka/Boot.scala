package us.marek.akka

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

@edu.umd.cs.findbugs.annotations.SuppressWarnings
object Boot extends App {
  
  implicit val system = ActorSystem.create("on-spray-can") 

  val service = system.actorOf(Props[MyServiceActor], "demo-service")

  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8888)
}
