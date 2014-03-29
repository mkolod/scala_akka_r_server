package us.marek.akka

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import akka.actor.ActorRef
import akka.pattern.ask
import akka.routing.Router
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import scala.io.Source
import org.json4s.JsonAST.JObject
import org.rosuda.REngine.Rserve.RConnection
import spray.httpx.Json4sSupport
import org.json4s.Formats
import org.json4s.DefaultFormats
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.Await

@edu.umd.cs.findbugs.annotations.SuppressWarnings
class MyServiceActor extends Actor with HttpService with Json4sSupport {

  var master: ActorRef = null

  implicit def json4sFormats: Formats = DefaultFormats
  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  override def preStart = {

    val actorCount = 4
    master = context.actorOf(Props(classOf[Master], actorCount))
  }

  val myRoute =
    path("rserve") {
      put {
        entity(as[JObject]) {
          request =>
            {
              respondWithMediaType(`application/json`) { 
                complete {
                  val future = Await.result(ask(master, request.extract[RRequest]), timeout.duration)
                  future.asInstanceOf[RResponse].msg
                }
              }
            }
        }
      }
    }

  def actorRefFactory = context
  def receive = runRoute(myRoute)

}
