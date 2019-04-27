package http

trait HttpResponse[+A] {
  def status: Int
  def statusText: String
  def bodyString: String
  def contentType: String
  def lastUri: String

  def headers: Headers
  def cookies: Cookies
}

sealed trait HttpSuccessfulResponse[A] extends HttpResponse[A] {
  def content: A
}

case class Ok[A](content: A, bodyString: String, contentType: String, lastUri: String, headers: Headers, cookies: Cookies) extends HttpSuccessfulResponse[A] {
  val status: Int = HttpStatus.Ok
  val statusText: String = "Ok"
}

case class NotFound(lastUri: String, bodyString: String, contentType: String, headers: Headers, cookies: Cookies) extends HttpResponse[Nothing] {
  val status : Int = HttpStatus.NotFound
  val statusText: String = "Not Found"
}

case class Forbidden(lastUri: String, bodyString: String, contentType: String, headers: Headers, cookies: Cookies) extends HttpResponse[Nothing]{
  val status : Int = HttpStatus.Forbidden
  val statusText: String = "Forbidden"
}

case class Unauthorized(lastUri: String, bodyString: String, contentType: String, headers: Headers, cookies: Cookies) extends HttpResponse[Nothing]{
  val status : Int = HttpStatus.Unauthorized
  val statusText: String = "Unauthorized"
}

case class UnhandledHttpStatus(status: Int,
                               statusText: String,
                               bodyString: String,
                               contentType: String,
                               lastUri: String,
                               headers: Headers,
                               cookies: Cookies) extends HttpResponse[Nothing]

object AuthenticationIssue {
  def unapply(arg: HttpResponse[_]): Option[(Int, String)] = {
    arg match {
      case f: Forbidden => Option(f.status, f.statusText)
      case u: Unauthorized => Option(u.status, u.statusText)
      case _ => None
    }
  }
}
