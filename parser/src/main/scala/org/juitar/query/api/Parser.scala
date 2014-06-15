package org.juitar.query.api

import org.juitar.query.api.model.{Filter, Order, Query, Select}

/**
 * @author sha1n
 * @since 6/12/14
 */
trait Parser {

  def parseQuery(select: Option[String] = None, order: Option[String] = None, filter: Option[String] = None): Query

  def parseSelect(select: String): Select

  def parseOrder(sort: String): Order

  def parseFilter(filter: String): Filter

}

object Parser {

  def get: Parser = {
    new ParserImpl
  }
}
