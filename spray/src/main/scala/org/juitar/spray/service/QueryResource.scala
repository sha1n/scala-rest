package org.juitar.spray.service

import org.juitar.query.api.model.Query
import spray.routing.HttpServiceBase

/**
 * @author sha1n
 * @since 6/30/14
 */
trait QueryResource extends HttpServiceBase {

  val resourcePath = path("echo-query") {
    get {
      parameters('filter.?, 'order.?, 'select.?) { (filter, order, select) => {

        val query = Query(select = select, filter = filter, order = order)
        complete(query.toString)
      }
      }
    }
  }
}
