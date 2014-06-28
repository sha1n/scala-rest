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
    val parser = Parser.instance(trace = true)
  }

  "parseSelect" should {

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

    "fail on null expression" in new Context {
      parser.parseSelect(null) must throwA[IllegalArgumentException]
    }

  }

  "parseFilter" should {

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

    "fail on invalid condition" in new Context {
      parser.parseFilter("a b > 1") must throwA[ParserError]
    }

    "fail on null condition" in new Context {
      parser.parseFilter(null) must throwA[IllegalArgumentException]
    }

  }

  "parseOrder" should {

    "succeed parsing basic order list" in new Context {
      parser.parseOrder("a, b, c") mustEqual Order(
        Seq(OrderExpression(Field("a")), OrderExpression(Field("b")), OrderExpression(Field("c"))))
    }

    "succeed parsing order list with direction" in new Context {
      parser.parseOrder("a desc, b, c desc") mustEqual Order(
        Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b"), OrderDirection.ASC), OrderExpression(Field("c"), OrderDirection.DESC)))
    }

    "fail on invalid order expression" in new Context {
      parser.parseOrder("a b ") must throwA[ParserError]
    }

    "fail on null expression" in new Context {
      parser.parseOrder(null) must throwA[IllegalArgumentException]
    }


  }

  "parseQuery" should {

    "succeed parsing with all segments" in new Context {
      val query = parser.parseQuery(Some("a,b"), Some("a desc, b"), Some("a ne c"))

      query mustEqual Query(
        Some(Select(Seq(Field("a"), Field("b")))),
        Some(Filter(ComparisonCondition(CompConditionOp.NE, Field("a"), Field("c")))),
        Some(Order(Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b")))))
      )

      query.isEmpty mustEqual false
    }

    "succeed with no parameters" in new Context {
      val query = parser.parseQuery(None, None, None)
      query mustEqual Query(None, None, None)
      query.isEmpty mustEqual true
    }

    "fail on invalid expression" in new Context {
      parser.parseQuery(Some("x,y"), Some("x x"), None) must throwA[ParserError]
    }
  }

  "implicit parser conversions" should {

    "convert all query segments to objects successfully" in new Context {
      val select: Select = "a, b"
      val filter: Filter = "a <= c"
      val order: Order = "a desc, b"

      select mustEqual Select(Seq(Field("a"), Field("b")))
      filter mustEqual Filter(ComparisonCondition(CompConditionOp.LTE, Field("a"), Field("c")))
      order mustEqual Order(Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b"))))
    }

    "convert all query segments to options successfully" in new Context {
      val query = Query(select = "a, b.c", filter = "a.e ne c", order = "a desc, b")

      query mustEqual Query(
        Some(Select(Seq(Field("a"), Field(Seq("b", "c"))))),
        Some(Filter(ComparisonCondition(CompConditionOp.NE, Field(Seq("a", "e")), Field("c")))),
        Some(Order(Seq(OrderExpression(Field("a"), OrderDirection.DESC), OrderExpression(Field("b")))))
      )
    }
  }

}
