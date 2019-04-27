package http.netty

import com.typesafe.config.Config
import org.asynchttpclient.AsyncHttpClient

class DefaultNettyHttpClient(protected val innerAsyncHttpClient: AsyncHttpClient) extends NettyHttpClient

object DefaultNettyHttpClient {

  def apply(config: Config): DefaultNettyHttpClient = {
    new DefaultNettyHttpClient(NettyHttpClient.asyncHttpClient(config))
  }
}
