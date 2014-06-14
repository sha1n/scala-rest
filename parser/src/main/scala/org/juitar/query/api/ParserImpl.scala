package org.juitar.query.api

import org.antlr.v4.runtime.{ANTLRInputStream, BailErrorStrategy, CommonTokenStream}
import org.juitar.query.api.model.{Filter, Order, Query, Select}
import org.juitar.query.generated.{QueryLexer, QueryParser}

/**
 * @author sha1n
 * @since 6/12/14
 *
 * @param trace ANTLR trace flag - for developer debug purposes only.
 */
class ParserImpl(trace: Boolean = false) extends Parser {

  def parseQuery(select: Option[String] = None, order: Option[String] = None, filter: Option[String] = None): Query = {
    new Query(
      select = select.map(s => {
        parseSelect(s)
      }),
      order = order.map(o => {
        parseOrder(o)
      }),
      filter = filter.map(f => {
        parseFilter(f)
      })
    )
  }

  def parseSelect(select: String): Select = {
    parseTree(new SelectParseHandler(createAntlrParser(select)))
  }

  def parseOrder(order: String): Order = {
    parseTree(new OrderParseHandler(createAntlrParser(order)))
  }

  def parseFilter(filter: String): Filter = {
    parseTree(new FilterParseHandler(createAntlrParser(filter)))
  }

  private def parseTree[T](parseHandler: AbstractParseHandler[T]): T = {
    try {
      parseHandler.parseTree
    }
    catch {
      case e: ParserException => throw e
      case e: Exception => throw new ParserError(e)
    }
  }

  /**
   * @param inputExpression the parser input expression
   * @return a `QueryParser` loaded with the input expression
   */
  private def createAntlrParser(inputExpression: String): QueryParser = {

    val inputStream = new ANTLRInputStream(inputExpression.toCharArray, inputExpression.length)
    val lexer = new QueryLexer(inputStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new QueryParser(tokens)

    if (trace) {
      parser.setBuildParseTree(true)
      parser.setTrace(true)
    }
    parser.setErrorHandler(new BailErrorStrategy)
    parser
  }

}

object ParserImpl {

  def main(args: Array[String]) {
    val parserImpl: ParserImpl = new ParserImpl(true)
    val filter = parserImpl.parseFilter("a > b and b < c or d != 1 or e = 'ssss'")
    val select = parserImpl.parseSelect("a,b,c.d, e.f.g")
    val order = parserImpl.parseOrder("a,b,c.d desc, e.f.g asc")
    println(filter)
    println(select)
    println(order)
  }
}
