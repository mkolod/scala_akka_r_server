package us.marek.akka

import akka.actor.Actor
import org.rosuda.REngine.Rserve.RConnection
import scala.io.Source

@edu.umd.cs.findbugs.annotations.SuppressWarnings
class RserveActor extends Actor {
  
  var c: RConnection = null
  
  override def preStart = {
    c = new RConnection()
    val preStartStr = Source.fromFile("./preStart.txt").getLines.mkString("\n")
    c eval preStartStr
  }
  
  override def postStop = c.close()
  
  def receive = {
    case message: RRequest => sender ! RResponse(c.eval(message.msg).asString)
    case _ =>
  }

}
