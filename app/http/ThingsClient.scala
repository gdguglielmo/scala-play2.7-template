package http

import http.netty.NettyHttpClient
import javax.inject.Inject
import model.{Thing, Thing2}
import org.asynchttpclient.AsyncHttpClient

import scala.concurrent.{ExecutionContext, Future}


class ThingsClient @Inject()(protected val innerAsyncHttpClient: AsyncHttpClient) extends NettyHttpClient {

  def findThingAge42(implicit ec: ExecutionContext): Future[HttpResponse[Seq[Thing2]]] = {
    performAs[Seq[Thing2]](HttpRequest("http://localhost:9000/things?age=42", HttpMethods.GET))
  }

}

class ThingClientException(message: String, cause: Throwable) extends RuntimeException(message, cause)
