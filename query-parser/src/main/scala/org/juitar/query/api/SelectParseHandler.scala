package org.juitar.query.api

import org.juitar.query.api.model._
import org.juitar.query.generated.QueryParser
import org.juitar.query.generated.QueryParser._

/**
 * @author sha1n
 * @since 6/13/14
 */
class SelectParseHandler(parser: QueryParser) extends AbstractParseHandler[Select](parser) {

  private final var select: Select = _

  override protected def handleParsing: Select = {
    parser.querySelect
    select
  }

  override def exitQuerySelect(ctx: QuerySelectContext) = {
    select = Select(elementStack.reverse.toSeq.asInstanceOf[Seq[Expression]])
  }
}
