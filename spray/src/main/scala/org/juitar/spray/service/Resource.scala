package org.juitar.spray.service

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
