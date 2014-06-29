package org.juitar.query

import org.juitar.query.api.model.{Filter, Order, Select}

/**
 * @author sha1n
 * @since 6/28/14
 */
package object api {

  implicit def asSelect(s: String): Select = Parser.instance.parseSelect(s)

  implicit def asFilter(f: String): Filter = Parser.instance.parseFilter(f)

  implicit def asOrder(o: String): Order = Parser.instance.parseOrder(o)

  implicit def asSelectOption(s: String): Option[Select] = Option(Parser.instance.parseSelect(s))

  implicit def asFilterOption(f: String): Option[Filter] = Option(Parser.instance.parseFilter(f))

  implicit def asOrderOption(o: String): Option[Order] = Option(Parser.instance.parseOrder(o))

  implicit def asSelectOption(expr: Option[String]): Option[Select] = expr match {
    case None => None
    case Some(s) => Some(Parser.instance.parseSelect(s))
  }

  implicit def asFilterOption(expr: Option[String]): Option[Filter] = expr match {
    case None => None
    case Some(s) => Some(Parser.instance.parseFilter(s))
  }

  implicit def asOrderOption(expr: Option[String]): Option[Order] = expr match {
    case None => None
    case Some(s) => Some(Parser.instance.parseOrder(s))
  }

}
