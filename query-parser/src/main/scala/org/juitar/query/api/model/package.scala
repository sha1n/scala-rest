package org.juitar.query.api

/**
 * @author sha1n
 * @since 6/13/14
 */
package object model {
  final val OP_VERBOSE_EQUAL = "eq"
  final val OP_VERBOSE_NOT_EQUAL = "ne"
  final val OP_VERBOSE_LESS = "lt"
  final val OP_VERBOSE_GREATER = "gt"
  final val OP_VERBOSE_LESS_OR_EQUAL = "le"
  final val OP_VERBOSE_GREATER_OR_EQUAL = "ge"
  final val OP_EQUAL = "="
  final val OP_NOT_EQUAL = "!="
  final val OP_LESS = "<"
  final val OP_GREATER = ">"
  final val OP_LESS_OR_EQUAL = "<="
  final val OP_GREATER_OR_EQUAL = ">="

  trait QueryElement

  case class Query(select: Option[Select], filter: Option[Filter], order: Option[Order]) extends QueryElement {
    def isEmpty = select.isEmpty && order.isEmpty && filter.isEmpty
  }

  case class Select(expr: Seq[Expression]) extends QueryElement {
    override def toString: String = expr.mkString(", ")
  }

  case class Filter(cond: Condition) extends QueryElement {
    override def toString: String = cond.toString
  }

  case class Order(orderExprs: Seq[OrderExpression]) extends QueryElement {
    override def toString: String = orderExprs.mkString(", ")
  }

  abstract class Condition extends QueryElement

  case class WrappedCondition(cond: Condition) extends Condition {
    override def toString: String = s"($cond)"
  }

  case class LogicalCondition(op: LogicalOp, conds: Seq[Condition]) extends Condition {
    override def toString: String = conds.mkString(s" ${op.toString} ")
  }

  case class ComparisonCondition(op: CompConditionOp, left: Expression, right: Expression) extends Condition {
    override def toString: String = s"$left $op $right"
  }

  abstract class Expression extends QueryElement

  case class Field(qualifiers: Seq[String]) extends Expression {
    override def toString: String = qualifiers.mkString(".")
  }

  object Field {
    def apply(name: String): Field = new Field(Seq(name))
  }

  case class OrderExpression(expr: Expression, dir: OrderDirection = OrderDirection.ASC) extends Expression {
    override def toString: String = s"$expr ${dir.toString}"
  }

  case class StringExpression(s: String) extends Expression {
    override def toString: String = s
  }

  case class NumberExpression(s: String) extends Expression {
    override def toString: String = s
  }
}
