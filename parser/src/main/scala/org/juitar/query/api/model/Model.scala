package org.juitar.query.api.model

/**
 * @author sha1n
 * @since 6/12/14
 */
trait QueryElement

case class Query(select: Select, filter: Filter, order: Order) extends QueryElement

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

case class OrderExpression(expr: Expression, dir: OrderDirection) extends Expression {
  override def toString: String = s"$expr ${dir.toString}"
}

case class StringExpression(s: String) extends Expression {
  override def toString: String = s
}

case class NumberExpression(s: String) extends Expression {
  override def toString: String = s
}
