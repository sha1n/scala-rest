package org.juitar.query.api

import org.antlr.v4.runtime.RecognitionException

/**
 * @author sha1n
 * @since 6/12/14
 */
class ParserException(format: String,
                      s: String,
                      e: RecognitionException,
                      tokenErrorDisplay: String,
                      errorHeader: String)
  extends RuntimeException {

}
