package http.netty

import http.{Cookie, Header, HttpRequest}
import io.netty.handler.codec.http.{HttpHeaders => AHCHeaders}
import org.asynchttpclient.cookie.{Cookie => AHCCookie}
import org.asynchttpclient.{RequestBuilder, Request => AHCRequest}

import scala.collection.JavaConverters.{asScalaBuffer, iterableAsScalaIterable}


trait NettyClientMappings {

  protected def toAsyncRequest(request: HttpRequest): AHCRequest = {
    val builder: RequestBuilder = new RequestBuilder

    builder.setUrl(request.uri.toString)

    request.headers.foreach{ h => builder.addHeader(h.name, h.value) }

    if (request.cookies.nonEmpty) {
      builder.setFollowRedirect(true)
      request.cookies.foreach(k => builder.addCookie(toAHCCookie(k)))
    }

    request.body.foreach(body => builder.setBody(body))

    builder.setMethod(request.method).build
  }

  protected def toAHCCookie(c: Cookie): AHCCookie = new AHCCookie(c.name, c.value, c.wrap, c.domain, c.path, c.maxAge, c.secure, c.httpOnly)

  protected def toCookie(c: AHCCookie): Cookie = Cookie(c.getName, c.getValue, c.isWrap, c.getDomain, c.getPath, c.getMaxAge, c.isSecure, c.isHttpOnly)

  // Keep implicit mapAsScalaMap to avoid recursion. Scala will try to use
  protected def toCookies(cookies: java.util.List[AHCCookie]): List[Cookie] = asScalaBuffer(cookies).toList.map(toCookie)

  protected def toHeaders(headers: AHCHeaders): List[Header] = iterableAsScalaIterable(headers).map(h => Header(h.getKey, h.getValue)).toList

}
