package org.juitar.query.api

import org.antlr.v4.runtime.{ANTLRInputStream, BailErrorStrategy, CommonTokenStream}
import org.juitar.query.api.model.{Filter, Order, Select}
import org.juitar.query.generated.{QueryLexer, QueryParser}

/**
 * @author sha1n
 * @since 6/12/14
 */
class ParserImpl extends Parser {

  private var antlrTrace = false

  def parseSelect(select: String): Select = {
    parseTree(new SelectParseHandler(createAntlrParser(select)))
  }

  def parseSort(sort: String): Order = {
    parseTree(new SortParseHandler(createAntlrParser(sort)))
  }

  def parseFilter(filter: String): Filter = {
    parseTree(new FilterParseHandler(createAntlrParser(filter)))
  }

  /**
   * Sets the ANTLR trace flag - for developer debug purposes only.
   *
   * @param antlrTrace the trace value, either { @code true} or { @code false}.
   */
  final def setAntlrTrace(antlrTrace: Boolean) {
    this.antlrTrace = antlrTrace
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

    if (antlrTrace) {
      parser.setBuildParseTree(true)
      parser.setTrace(true)
    }
    parser.setErrorHandler(new BailErrorStrategy)
    parser
  }

}

object ParserImpl {

  def main(args: Array[String]) {
    val parserImpl: ParserImpl = new ParserImpl
    val filter = parserImpl.parseFilter("a > b and b < c or d != 1 or e = 'ssss'")
    val select = parserImpl.parseSelect("a,b,c.d, e.f.g")
    val order = parserImpl.parseSort("a,b,c.d desc, e.f.g asc")
    println(filter)
    println(select)
    println(order)
  }
}
