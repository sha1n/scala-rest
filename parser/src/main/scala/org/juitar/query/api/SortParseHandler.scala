package org.juitar.query.api

import org.juitar.query.api.model._
import org.juitar.query.generated.QueryParser
import org.juitar.query.generated.QueryParser._

/**
 * @author sha1n
 * @since 6/13/14
 */
class SortParseHandler(parser: QueryParser) extends AbstractParseHandler[Order](parser) {

  private final var order: Order = _

  override protected def handleParsing: Order = {
    parser.queryOrder
    order
  }

  override def exitOrderItem(ctx: OrderItemContext) {
    val orderExpression = new OrderExpression(
      elementStack.pop().asInstanceOf[Expression],
      if (ctx.DESC != null) OrderDirection.DESC else OrderDirection.ASC
    )
    elementStack.push(orderExpression)
  }

  override def exitQueryOrder(ctx: QueryOrderContext) {
    order = Order(elementStack.reverse.toSeq.asInstanceOf[Seq[OrderExpression]])
  }
}