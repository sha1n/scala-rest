package org.juitar.query.api

/**
 * @author sha1n
 * @since 6/13/14
 */
case class ParserError(message: String, e: Exception) extends RuntimeException(message, e) {
  def this(e: Exception) = this(null, e)

  def this(m: String) = this(m, null)
}
