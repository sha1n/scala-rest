package org.juitar.query.api

import org.juitar.query.api.model.{Field, NumberExpression, QueryElement, StringExpression}
import org.juitar.query.generated.QueryParser.{FactorNumberExpressionContext, PropertyExpressionContext, StringExpressionContext}
import org.juitar.query.generated.{QueryParser, QueryParserBaseListener}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * @author sha1n
 * @since 6/12/14
 */
abstract class AbstractParseHandler[T](parser: QueryParser) extends QueryParserBaseListener {

  protected final val elementStack: mutable.Stack[QueryElement] = new mutable.Stack[QueryElement]

  /**
   * This method calls the appropriate ANTLR 4 generated `Parser` method, to parse the relevant expression and returns
   * the appropriate tree construct.
   *
   * @return an instance of the generic type which represents a query tree construct.
   */
  protected def handleParsing: T

  /**
   * This method is called by the `ParserImpl` instance in charge of the current parsing process to parse
   * a query expression into a tree construct.
   *
   * @return an instance of the generic type which represents a query tree construct.
   *
   * @see `handleParsing`
   */
  final def parseTree: T = {
    parser.addParseListener(this)
    handleParsing
  }

  override final def exitStringExpression(ctx: StringExpressionContext) {
    elementStack.push(new StringExpression(ctx.getText))
  }

  override final def exitFactorNumberExpression(ctx: FactorNumberExpressionContext) {
    elementStack.push(new NumberExpression(ctx.getText))
  }

  override final def exitPropertyExpression(ctx: PropertyExpressionContext) =
    elementStack.push(new Field(ctx.IDENTIFIER.map(tn => tn.getText)))

}
