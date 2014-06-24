package org.juitar.query.api

import java.util.concurrent.Executors

import org.juitar.query.api.model._

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
 * @author sha1n
 * @since 6/15/14
 */
private[api] object ParseExecutor {

  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors()))

  final val DefaultTimeout = 1000

  def parseQuery(select: Option[String], filter: Option[String], order: Option[String], timeoutMillis: Long = DefaultTimeout): Query = {

    val selectFuture = select match {
      case None => Future.successful(None)
      case Some(s) =>
        val selectF = Future {
          Some(Parser.get.parseSelect(s))
        }

        selectF onComplete {
          case Success(r) =>
          case Failure(t) => throw t
        }

        selectF

    }

    val filterFuture = filter match {
      case None => Future.successful(None)
      case Some(f) =>
        val filterF = Future {
          Some(Parser.get.parseFilter(f))
        }

        filterF onComplete {
          case Success(r) =>
          case Failure(t) => throw t
        }

        filterF

    }

    val orderFuture = order match {
      case None => Future.successful(None)
      case Some(o) =>
        val orderF = Future {
          Some(Parser.get.parseOrder(o))
        }

        orderF onComplete {
          case Success(r) =>
          case Failure(t) => throw t
        }

        orderF

    }

    Query(
      select = Await.result(selectFuture, Duration(timeoutMillis, "ms")),
      filter = Await.result(filterFuture, Duration(timeoutMillis, "ms")),
      order = Await.result(orderFuture, Duration(timeoutMillis, "ms")))
  }

}
