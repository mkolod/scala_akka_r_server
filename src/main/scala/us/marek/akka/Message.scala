package us.marek.akka

sealed trait Message extends Serializable

case class LatencyThroughput(latency: Double, numRequests: Long) extends Message
case class RResponse(msg: String) extends Message
case class RRequest(msg: String) extends Message
case object Terminate extends Message