package org.juitar.spray

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.juitar.spray.service.ResourceImpl
import spray.can.Http

import scala.concurrent.duration._


/**
 * @author shain
 * @since 6/25/14
 */
object Launcher extends App {

  implicit val system = ActorSystem("spray-can-server")
  implicit val timeout = Timeout(5.seconds)

  val serviceActor = system.actorOf(Props[ResourceImpl], "demo-service")

  // todo: need to figure out what that statement is exactly...
  IO(Http) ? Http.Bind(serviceActor, interface = "localhost", port = 8080)
}
