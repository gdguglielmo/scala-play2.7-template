package http.netty

import http.{HttpClient, HttpRequest, HttpResponse}
import org.asynchttpclient.{AsyncHttpClient, DefaultAsyncHttpClient, DefaultAsyncHttpClientConfig}
import play.api.Configuration
import play.api.libs.json.Reads

import scala.concurrent.{ExecutionContext, Future, Promise}

/**
  * Netty based HttpClient
  */
trait NettyHttpClient extends HttpClient
                         with NettyClientMappings {

  protected def innerAsyncHttpClient: AsyncHttpClient

  def close(): Unit = innerAsyncHttpClient.close()

  def performAs[A](request: HttpRequest)(implicit reads: Reads[A], ec: ExecutionContext): Future[HttpResponse[A]] = {
    val promise = Promise[HttpResponse[A]]

    innerAsyncHttpClient.executeRequest(toAsyncRequest(request), new DefaultAsyncCompletionHandler(promise))

    promise.future
  }

}

object NettyHttpClient {
  def asyncHttpClient(config: Configuration): AsyncHttpClient = {
    val clientConfig = new DefaultAsyncHttpClientConfig.Builder()
      .setCompressionEnforced(false)
      .setConnectionTtl(0)
      .setReadTimeout(config.get[Int]("http-client.read-timeout"))
      .setConnectTimeout(config.get[Int]("http-client.connection-timeout"))
      .setMaxConnections(config.get[Int]("http-client.max-connections"))
      .setMaxRedirects(config.get[Int]("http-client.max-redirects"))
      .setSslSessionTimeout(config.get[Int]("http-client.session-timeout"))
      .setMaxRequestRetry(3)
      .setUseProxyProperties(false) // Do not use the proxy defined in program JVM parameters
      .build

    new DefaultAsyncHttpClient(clientConfig)
  }
}
