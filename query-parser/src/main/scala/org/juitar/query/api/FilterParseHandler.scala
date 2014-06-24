package org.juitar.query.api

import org.juitar.query.api.model._
import org.juitar.query.generated.QueryParser
import org.juitar.query.generated.QueryParser._

import scala.collection.mutable

/**
 * @author sha1n
 * @since 6/13/14
 */
class FilterParseHandler(parser: QueryParser) extends AbstractParseHandler[Filter](parser) {

  private final var queryFilter: Filter = _

  override def exitConditionAnd(ctx: ConditionAndContext) {
    if (!ctx.AND.isEmpty) {
      val conditionCount = ctx.simpleCondition.size
      val conditions = popFirst(conditionCount)
      val logicalCondition = new LogicalCondition(LogicalOp.AND, conditions)
      elementStack.push(logicalCondition)
    }
  }

  override def exitSimpleCondition(ctx: SimpleConditionContext) {
    if (ctx.LPAREN != null) {
      val wrappedCondition = new WrappedCondition(elementStack.pop().asInstanceOf[Condition])
      elementStack.push(wrappedCondition)
    }
  }

  override def exitSimpleComparisonCondition(ctx: SimpleComparisonConditionContext) {
    val right = elementStack.pop().asInstanceOf[Expression]
    val left = elementStack.pop().asInstanceOf[Expression]
    val comparisonCondition =
      new ComparisonCondition(getComparisonOperator(ctx.comparisonOperator.getText.toLowerCase), left, right)
    elementStack.push(comparisonCondition)
  }

  override def exitCondition(ctx: ConditionContext) {
    if (!ctx.OR.isEmpty) {
      val conditionCount = ctx.conditionAnd.size
      val conditions = popFirst(conditionCount)
      val logicalCondition = new LogicalCondition(LogicalOp.OR, conditions)
      elementStack.push(logicalCondition)
    }
  }

  override def exitQueryFilter(ctx: QueryFilterContext) {
    queryFilter = Filter(elementStack.pop().asInstanceOf[Condition])
  }

  override protected def handleParsing: Filter = {
    parser.queryFilter
    queryFilter
  }

  private def popFirst[T <: QueryElement](n: Int): Seq[T] = {
    val parts: (mutable.Stack[QueryElement], mutable.Stack[QueryElement]) = elementStack.splitAt(n)

    elementStack.clear()
    elementStack.pushAll(parts._2)

    parts._1.reverse.toSeq.asInstanceOf[Seq[T]]
  }

  private def getComparisonOperator(parsedOperatorString: String): CompConditionOp = {
    parsedOperatorString match {
      case OP_EQUAL =>
        CompConditionOp.EQ
      case OP_NOT_EQUAL =>
        CompConditionOp.NE
      case OP_LESS =>
        CompConditionOp.LT
      case OP_GREATER =>
        CompConditionOp.GT
      case OP_LESS_OR_EQUAL =>
        CompConditionOp.LTE
      case OP_GREATER_OR_EQUAL =>
        CompConditionOp.GTE
      case OP_VERBOSE_EQUAL =>
        CompConditionOp.EQ
      case OP_VERBOSE_NOT_EQUAL =>
        CompConditionOp.NE
      case OP_VERBOSE_LESS =>
        CompConditionOp.LT
      case OP_VERBOSE_GREATER =>
        CompConditionOp.GT
      case OP_VERBOSE_LESS_OR_EQUAL =>
        CompConditionOp.LTE
      case OP_VERBOSE_GREATER_OR_EQUAL =>
        CompConditionOp.GTE
      case _ =>
        throw new ParserException(parsedOperatorString + " is not a valid comparison operator. This is a bug!")
    }
  }
}
