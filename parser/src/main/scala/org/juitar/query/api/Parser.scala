package org.juitar.query.api

import org.juitar.query.api.model.{Filter, Order, Select}

/**
 * @author sha1n
 * @since 6/12/14
 */
trait Parser {

  def parseSelect(select: String): Select

  def parseOrder(sort: String): Order

  def parseFilter(filter: String): Filter

}
