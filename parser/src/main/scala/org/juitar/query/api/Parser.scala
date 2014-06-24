package org.juitar.query.api

import org.juitar.query.api.model.{Filter, Order, Query, Select}

/**
 * @author sha1n
 * @since 6/12/14
 */
trait Parser {

  /**
   * Parses all the input query segments into a [[Query]] object.
   *
   * @param select an optional 'select' expression
   * @param order an optional 'order' expression
   * @param filter an optional 'filter' expression
   *
   * @return an instance of [[Query]] containing the object representation of the non-[[None]] input expressions.
   */
  def parseQuery(select: Option[String] = None, order: Option[String] = None, filter: Option[String] = None): Query

  /**
   * Parses the specified select expression into a [[Select]] object
   * @param select a query select expression
   * @return [[Select]]
   */
  def parseSelect(select: String): Select

  /**
   * Parses the specified order expression into an [[Order]] object
   * @param order a query order expression
   * @return [[Order]]
   */
  def parseOrder(order: String): Order

  /**
   * Parses the specified filter expression into an [[Filter]] object
   * @param filter a query filter expression
   * @return [[Filter]]
   */
  def parseFilter(filter: String): Filter

}

object Parser {

  /**
   * Factory method for [[Parser]] instances
   * @return [[Parser]]
   */
  def instance: Parser = new ParserImpl

  /**
   * Factory method for [[Parser]] instances
   * @param trace turns on/off parser tracing. Use for debug purposes.
   * @return [[Parser]]
   */
  def instance(trace: Boolean) = new ParserImpl(trace)
}
