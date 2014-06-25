package org.juitar.query.api

import java.util.concurrent.Executors

import org.juitar.query.api.model.{Filter, Order, Query, Select}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

/**
 * @author sha1n
 * @since 6/15/14
 */
private[api] object ParseExecutor {

  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors()))

  private final val DefaultTimeout = 1000

  def parseQuery(select: Option[String], filter: Option[String], order: Option[String], timeoutMillis: Long = DefaultTimeout): Query = {


    def handleParse[T]: (Try[Some[T]]) => Unit = {
      case Success(r) =>
      case Failure(t) => throw t
    }

    val dummyFuture = Future.successful(None)

    val selectFuture = select match {
      case None => dummyFuture
      case Some(s) =>

        val selectF = Future {
          Some(Parser.instance.parseSelect(s))
        }

        selectF onComplete handleParse[Select]
        selectF

    }

    val filterFuture = filter match {
      case None => dummyFuture
      case Some(f) =>

        val filterF = Future {
          Some(Parser.instance.parseFilter(f))
        }

        filterF onComplete handleParse[Filter]
        filterF

    }

    val orderFuture = order match {
      case None => dummyFuture
      case Some(o) =>

        val orderF = Future {
          Some(Parser.instance.parseOrder(o))
        }

        orderF onComplete handleParse[Order]
        orderF

    }

    val timeout = Duration(timeoutMillis, MILLISECONDS)

    Query(
      select = Await.result(selectFuture, timeout),
      filter = Await.result(filterFuture, timeout),
      order = Await.result(orderFuture, timeout))
  }

}
