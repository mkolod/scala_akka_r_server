package us.marek.akka

import akka.actor.Actor
import java.sql.Time

class ResponseListener extends Actor {
  
  def receive = {
    case msg: String =>
      println("Got message from " + sender + " at " + new Time(System.currentTimeMillis) + ": " + msg)
    case _ =>
  }
  
}