package org.juitar.query.api

import org.juitar.query.api.model._
import org.specs2.mutable._

/**
 * @author sha1n
 * @since 6/13/14
 */
class ParserImplTest extends SpecificationWithJUnit {

  val parser = new ParserImpl

  "select" should {
    "succeed parsing basic field list" in {
      parser.parseSelect("a, b, c") mustEqual Select(Seq(Field("a"), Field("b"), Field("c")))
    }

    "succeed parsing multipart fields list" in {
      parser.parseSelect("a.d.e, b, c").toString mustEqual Select(Seq(Field("a.d.e"), Field("b"), Field("c"))).toString
    }

    "succeed parsing number and string expression list" in {
      parser.parseSelect("1, 'str', c") mustEqual Select(Seq(NumberExpression("1"), StringExpression("'str'"), Field("c")))
    }

  }

  "filter" should {
    "succeed parsing basic condition" in {
      parser.parseFilter("a < 1") mustEqual Filter(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")))
    }

    "succeed parsing logical condition" in {
      parser.parseFilter("a < 1 and a > 0") mustEqual Filter(LogicalCondition(LogicalOp.AND,
        Seq(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")),
          ComparisonCondition(CompConditionOp.GT, Field("a"), NumberExpression("0")))))
    }

    "succeed parsing wrapped logical condition" in {
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

  }

  "order" should {
    "succeed parsing basic order list" in {
      parser.parseOrder("a, b, c") mustEqual Order(
        Seq(OrderExpression(Field("a")), OrderExpression(Field("b")), OrderExpression(Field("c"))))
    }

    "succeed parsing order list with direction" in {
      parser.parseOrder("a desc, b, c desc") mustEqual Order(
        Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b"), OrderDirection.ASC), OrderExpression(Field("c"), OrderDirection.DESC)))
    }
  }

  "query" should {
    "succeed parsing with all segments" in {
      parser.parseQuery(Some("a,b"), Some("a desc, b"), Some("a <= c")) mustEqual Query(
        Some(Select(Seq(Field("a"), Field("b")))),
        Some(Filter(ComparisonCondition(CompConditionOp.LTE, Field("a"), Field("c")))),
        Some(Order(Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b")))))
      )
    }

    "succeed no parameters" in {
      parser.parseQuery(None, None, None) mustEqual Query(None, None, None)
    }
  }

}
