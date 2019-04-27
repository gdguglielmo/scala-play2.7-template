package http

case class Header(name: String, value: String) {
  def toTuple: (String, String) = name -> value
}

object Header {

  def oauthToken(rawToken: String): Header ={
    val tokenValue = s"Bearer $rawToken"
    Header("Authorization", tokenValue)
  }
}

case class Cookie(name: String, value: String, wrap: Boolean, domain: String, path: String, maxAge: Long, secure: Boolean, httpOnly: Boolean)

object HttpStatus {
  val Ok = 200
  val Unauthorized = 401
  val Forbidden = 403
  val NotFound = 404
  val InternalServerError = 500
  val ServiceUnavailable = 503
}

object HttpMethods {
  val GET = "GET"
  val POST = "POST"
  val PUT = "PUT"
  val PATCH = "PATCH"
  val DELETE = "DELETE"
  val OPTIONS = "OPTIONS"
}
