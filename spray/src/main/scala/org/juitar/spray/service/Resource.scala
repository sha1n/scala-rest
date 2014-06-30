package org.juitar.spray.service

import java.lang.management.ManagementFactory

import org.juitar.query.api.model.Query
import spray.http.MediaTypes._
import spray.httpx.marshalling.ToResponseMarshallable._
import spray.routing.HttpService

/**
 * @author shain
 * @since 6/25/14
 */
trait Resource extends HttpService {

  val root =
    path("") {
      get {
        respondWithMediaType(`text/plain`) {
          complete {
            "I'm alive"
          }
        }
      }
    } ~
      path("threads") {
        get {
          val tmb = ManagementFactory.getThreadMXBean

          import tmb._

          val info = getThreadInfo(getAllThreadIds)

          complete(
            getThreadCount.toString + "\r\n" +
              info.sortBy(ti => ti.getThreadName).mkString)
        }
      } ~
      path("echo-query") {
        get {
          parameters('filter.?, 'order.?, 'select.?) { (filter, order, select) => {

            val query = Query(select = select, filter = filter, order = order)
            complete(query.toString)
          }
          }
        }
      }
}
