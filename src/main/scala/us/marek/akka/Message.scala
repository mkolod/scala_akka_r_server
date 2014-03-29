package us.marek.akka

import edu.umd.cs.findbugs.annotations.{SuppressWarnings => SupWarn}

@SupWarn
sealed trait Message extends Serializable

@SupWarn
case class RResponse(msg: String) extends Message

@SupWarn
case class RRequest(msg: String) extends Message

@SupWarn
case object Terminate extends Message
