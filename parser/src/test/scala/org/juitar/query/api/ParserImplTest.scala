package org.juitar.query.api

import org.juitar.query.api.model._
import org.specs2.mutable._

/**
 * @author sha1n
 * @since 6/13/14
 */
//@RunWith(classOf[SpecificationWithJUnit])
class ParserImplTest extends SpecificationWithJUnit {
  //Specification {

  "select" should {
    "succeed parsing basic field list" in {
      val parser: ParserImpl = new ParserImpl
      parser.parseSelect("a, b, c") mustEqual Select(Seq(Field("a"), Field("b"), Field("c")))
    }

    "succeed parsing multipart fields list" in {
      val parser: ParserImpl = new ParserImpl
      parser.parseSelect("a.d.e, b, c") mustEqual Select(Seq(Field("a.d.e"), Field("b"), Field("c")))
    }.pendingUntilFixed("Specs2 claims not equal for some reason")

    "succeed parsing number and string expression list" in {
      val parser: ParserImpl = new ParserImpl
      parser.parseSelect("1, 'str', c") mustEqual Select(Seq(NumberExpression("1"), StringExpression("'str'"), Field("c")))
    }

  }

  "filter" should {
    "succeed parsing basic condition" in {
      val parser: ParserImpl = new ParserImpl
      parser.parseFilter("a < 1") mustEqual Filter(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")))
    }

    "succeed parsing logical condition" in {
      val parser: ParserImpl = new ParserImpl
      parser.parseFilter("a < 1 and a > 0") mustEqual Filter(LogicalCondition(LogicalOp.AND,
        Seq(ComparisonCondition(CompConditionOp.LT, Field("a"), NumberExpression("1")),
          ComparisonCondition(CompConditionOp.GT, Field("a"), NumberExpression("0")))))
    }

    "succeed parsing wrapped logical condition" in {
      val parser: ParserImpl = new ParserImpl
      parser.parseFilter("(a < 1 and a > 0) or (b = c and d != 'kuki')") mustEqual Filter(
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
      )
    }.pendingUntilFixed("Specs2 claims not equal")

  }
}
