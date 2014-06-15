package org.juitar.query.api

import org.juitar.query.api.model._
import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * @author sha1n
 * @since 6/13/14
 */
class ParserTest extends SpecificationWithJUnit {

  trait Context extends Scope {
    val parser = Parser.get
  }

  "select" should {
    "succeed parsing basic field list" in new Context {
      parser.parseSelect("a, b, c") mustEqual Select(Seq(Field("a"), Field("b"), Field("c")))
    }

    "succeed parsing multipart fields list" in new Context {
      parser.parseSelect("a.d.e, b, c").toString mustEqual Select(Seq(Field("a.d.e"), Field("b"), Field("c"))).toString
    }

    "succeed parsing number and string expression list" in new Context {
      parser.parseSelect("1, 'str', c") mustEqual Select(Seq(NumberExpression("1"), StringExpression("'str'"), Field("c")))
    }

    "fail on illegal select statement" in new Context {
      parser.parseSelect("a b ") must throwA[ParserError]
    }

    "fail on illegal select statement" in new Context {
      parser.parseSelect("a > b ") must throwA[ParserError]
    }

  }

  "filter" should {
    "succeed parsing basic condition" in new Context {
      parser.parseFilter("a < 1") mustEqual Filter(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")))
    }

    "succeed parsing logical condition" in new Context {
      parser.parseFilter("a < 1 and a > 0") mustEqual Filter(LogicalCondition(LogicalOp.AND,
        Seq(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")),
          ComparisonCondition(CompConditionOp.GT, Field("a"), NumberExpression("0")))))
    }

    "succeed parsing wrapped logical condition" in new Context {
      parser.parseFilter("(a < 1 and a > 0) or (b = c and d != 'kuki')").toString mustEqual Filter(
        LogicalCondition(LogicalOp.OR,
          Seq(
            WrappedCondition(LogicalCondition(LogicalOp.AND,
              Seq(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")),
                ComparisonCondition(CompConditionOp.GT, Field("a"), NumberExpression("0"))))),
            WrappedCondition(LogicalCondition(LogicalOp.AND,
              Seq(ComparisonCondition(CompConditionOp.EQ, Field("b"), NumberExpression("c")),
                ComparisonCondition(CompConditionOp.NE, Field("d"), StringExpression("'kuki'")))))
          )
        )
      ).toString
    }

    "fail on invalid statement" in new Context {
      parser.parseFilter("a b > 1") must throwA[ParserError]
    }

  }

  "order" should {
    "succeed parsing basic order list" in new Context {
      parser.parseOrder("a, b, c") mustEqual Order(
        Seq(OrderExpression(Field("a")), OrderExpression(Field("b")), OrderExpression(Field("c"))))
    }

    "succeed parsing order list with direction" in new Context {
      parser.parseOrder("a desc, b, c desc") mustEqual Order(
        Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b"), OrderDirection.ASC), OrderExpression(Field("c"), OrderDirection.DESC)))
    }

    "fail on illegal order list" in new Context {
      parser.parseOrder("a b ") must throwA[ParserError]
    }

    "fail on illegal order list" in new Context {
      parser.parseOrder("a > b ") must throwA[ParserError]
    }
  }

  "query" should {
    "succeed parsing with all segments" in new Context {
      parser.parseQuery(Some("a,b"), Some("a desc, b"), Some("a <= c")) mustEqual Query(
        Some(Select(Seq(Field("a"), Field("b")))),
        Some(Filter(ComparisonCondition(CompConditionOp.LTE, Field("a"), Field("c")))),
        Some(Order(Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b")))))
      )
    }

    "succeed no parameters" in new Context {
      parser.parseQuery(None, None, None) mustEqual Query(None, None, None)
    }
  }

}
