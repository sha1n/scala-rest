package org.juitar.spray.service

import org.juitar.query.api.model.Query
import org.specs2.mutable.SpecificationWithJUnit
import spray.http.StatusCodes._
import spray.testkit.Specs2RouteTest

/**
 * @author shain
 * @since 6/25/14
 */
class ResourceTest extends SpecificationWithJUnit with Specs2RouteTest with Resource {
  def actorRefFactory = system

  "RestService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> root ~> check {
        responseAs[String] must contain("I'm alive")
      }
    }


    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(root) ~> check {
        status === MethodNotAllowed
      }
    }

    // todo: why response status cannot be evaluated? Client does get the expected 404
    "leave GET requests to other paths unhandled" in {
      Get("/404") ~> root ~> check {
        handled must beFalse
      }
    }
  }

  "echo-query" should {
    "echo parsed query expression" in {
      Get("/echo-query?select=a,b.c&filter=a>b&order=a,b") ~> root ~> check {
        responseAs[String] mustEqual Query(select = "a,b.c", filter = "a>b", order = "a,b").toString
      }
    }
  }
}
