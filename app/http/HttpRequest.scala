package http

import akka.http.scaladsl.model.Uri

case class HttpRequest(uri: Uri,
                       method: String = HttpMethods.GET,
                       headers: Headers = Nil,
                       cookies: Cookies = Nil,
                       body: Option[Body] = None)
