package org.juitar.query.api


/**
 * @author sha1n
 * @since 6/12/14
 */
class ParserException(message: String, e: Exception)
  extends RuntimeException(message, e) {

  def this(e: Exception) = this(null, e)

  def this(message: String) = this(message, null)
}
