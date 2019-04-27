package http

import play.api.libs.json.{JsSuccess, Reads}

import scala.concurrent.{ExecutionContext, Future}

/**
  * HttpClient implementation on any supported framework
  */
trait HttpClient {

  private implicit val unitReads: Reads[Unit] = Reads(_ => JsSuccess(()))

  /**
    * Performs an HttpRequest and returns its ResponseBody as A
    */
  def performAs[A](request: HttpRequest)(implicit reads: Reads[A], ec: ExecutionContext): Future[HttpResponse[A]]

  /**
    * Performs an HttpRequest and returns its ResponseBody as String
    */
  def performAsString(request: HttpRequest)(implicit ec: ExecutionContext): Future[HttpResponse[String]] = {
    performAs[String](request)
  }

  /**
    * Performs an HttpRequest and ignore its ResponseBody
    */
  def performAsUnit(request: HttpRequest)(implicit ec: ExecutionContext): Future[HttpResponse[Unit]] = {
    performAs[Unit](request)
  }

  def close(): Unit

}
