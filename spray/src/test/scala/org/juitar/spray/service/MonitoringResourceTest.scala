package org.juitar.spray.service

import org.specs2.mutable.SpecificationWithJUnit
import spray.http.StatusCodes._
import spray.testkit.Specs2RouteTest

/**
 * @author shain
 * @since 6/25/14
 */
class MonitoringResourceTest extends SpecificationWithJUnit with Specs2RouteTest with MonitoringResource {
  def actorRefFactory = system

  "MonitoringResource" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> resourcePath ~> check {
        responseAs[String] must contain("I'm alive")
      }
    }


    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(resourcePath) ~> check {
        status === MethodNotAllowed
      }
    }

    // todo: why response status cannot be evaluated? Client does get the expected 404
    "leave GET requests to other paths unhandled" in {
      Get("/404") ~> resourcePath ~> check {
        handled must beFalse
      }
    }
  }

}
