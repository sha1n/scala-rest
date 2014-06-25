package org.juitar.spray.service

import spray.http.MediaTypes._
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
    }
}
