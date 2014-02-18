package us.marek.akka

import akka.actor.Actor

class BenchmarkListener extends Actor {
  
  def receive = {
    case l: LatencyThroughput => {
			println(f"\nper request latency = ${l.latency}%.3f ms," +
			        f" per second throughput = ${1000.0/l.latency }%.0f \n")
    }
    case _      => 
  } 
}