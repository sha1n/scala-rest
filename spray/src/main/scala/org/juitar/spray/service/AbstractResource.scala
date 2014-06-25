package org.juitar.spray.service

import akka.actor.Actor

/**
 * @author shain
 * @since 6/25/14
 */
abstract class AbstractResource extends Actor {

  def actorRefFactory = context

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = println("starting...")

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = println("stopped!")
}
