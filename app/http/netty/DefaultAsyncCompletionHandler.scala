package http.netty

import java.nio.charset.{Charset, StandardCharsets}

import http.{Forbidden, HttpResponse, HttpStatus, NotFound, Ok, Unauthorized, UnhandledHttpStatus}
import org.asynchttpclient.util.HttpUtils
import org.asynchttpclient.{AsyncCompletionHandler, Response => AHCResponse}
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}

import scala.concurrent.{ExecutionContext, Promise}

class DefaultAsyncCompletionHandler[A](promise: Promise[HttpResponse[A]])(implicit reads: Reads[A],
                                                                          executionContext: ExecutionContext) extends AsyncCompletionHandler[AHCResponse]
                                                                                                                 with NettyClientMappings {

  def onCompleted(response: AHCResponse): AHCResponse = {

    val uri = response.getUri.toString
    val charset = charsetFor(response).getOrElse(StandardCharsets.UTF_8)
    val responseBody = response.getResponseBody(charset)
    val contentType = response.getContentType
    val headers = toHeaders(response.getHeaders)
    val cookies = toCookies(response.getCookies)
    val statusText = response.getStatusText

    response.getStatusCode match {
      case HttpStatus.Ok =>
        Json.fromJson[A](Json.parse(responseBody)) match {
          case JsSuccess(value, _) => promise.success(Ok(value, responseBody, contentType, uri, headers, cookies))
          case JsError(errors) => promise.failure(new IllegalArgumentException(s"Invalid json object: $errors"))
        }
      case HttpStatus.NotFound =>
        promise.success(NotFound(uri, responseBody, contentType, headers, cookies))
      case HttpStatus.Forbidden =>
        promise.success(Forbidden(uri, responseBody, contentType, headers, cookies))
      case HttpStatus.Unauthorized =>
        promise.success(Unauthorized(uri, responseBody, contentType, headers, cookies))
      case status =>
        promise.success(UnhandledHttpStatus(status, statusText, responseBody, contentType, uri, headers, cookies))
    }

    response
  }

  override def onThrowable(t: Throwable) {
    promise.failure(t)
  }

  private def charsetFor(response: AHCResponse): Option[Charset] = for {
    contentType <- Option(response.getContentType)
    charSet <- Option(HttpUtils.extractContentTypeCharsetAttribute(contentType))
  } yield charSet
}
