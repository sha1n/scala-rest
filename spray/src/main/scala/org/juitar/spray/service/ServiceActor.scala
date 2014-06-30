package org.juitar.spray.service

import akka.util.Timeout
import spray.http.{HttpResponse, StatusCodes}
import spray.routing.HttpService

import scala.concurrent.duration.Duration

/**
 * @author sha1n
 * @since 6/30/14
 */
class ServiceActor extends AbstractResource with HttpService {

  def receive = handleTimeouts orElse runRoute(
    new MonitoringResourceImpl().route ~ new QueryResourceImpl().route)

  def handleTimeouts: Receive = {
    case Timeout(d: Duration) =>
      sender ! HttpResponse(StatusCodes.InternalServerError, "Request timed out.")
  }
}
