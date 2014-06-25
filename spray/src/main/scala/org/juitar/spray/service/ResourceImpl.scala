package org.juitar.spray.service

/**
 * @author shain
 * @since 6/25/14
 */
class ResourceImpl extends AbstractResource with Resource {

  def receive = runRoute(root)

}
