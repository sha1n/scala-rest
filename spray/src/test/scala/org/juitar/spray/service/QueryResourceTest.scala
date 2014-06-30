package org.juitar.spray.service

import org.juitar.query.api.model.Query
import org.specs2.mutable.SpecificationWithJUnit
import spray.testkit.Specs2RouteTest

/**
 * @author shain
 * @since 6/25/14
 */
class QueryResourceTest extends SpecificationWithJUnit with Specs2RouteTest with QueryResource {
  def actorRefFactory = system

  "QueryResource" should {

    "echo-query" should {
      "echo parsed query expression" in {
        Get("/echo-query?select=a,b.c&filter=a>b&order=a,b") ~> resourcePath ~> check {
          responseAs[String] mustEqual Query(select = "a,b.c", filter = "a>b", order = "a,b").toString
        }
      }
    }
  }
}
