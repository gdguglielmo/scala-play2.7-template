package http.netty

import http.{Cookie, Header, HttpRequest}
import io.netty.handler.codec.http.{HttpHeaders => AHCHeaders}
import io.netty.handler.codec.http.cookie.{Cookie => AHCCookie, DefaultCookie => DefaultAHCCookie}
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

  protected def toAHCCookie(c: Cookie): AHCCookie = {
    val cookie = new DefaultAHCCookie(c.name, c.value)
    cookie.setWrap(c.wrap); cookie.setDomain(c.domain); cookie.setPath(c.path); cookie.setMaxAge(c.maxAge); cookie.setSecure(c.secure);cookie.setHttpOnly(c.httpOnly)
    cookie
  }

  protected def toCookie(c: AHCCookie): Cookie = Cookie(c.name, c.value, c.wrap, c.domain, c.path, c.maxAge, c.isSecure, c.isHttpOnly)

  // Keep implicit mapAsScalaMap to avoid recursion. Scala will try to use
  protected def toCookies(cookies: java.util.List[AHCCookie]): List[Cookie] = asScalaBuffer(cookies).toList.map(toCookie)

  protected def toHeaders(headers: AHCHeaders): List[Header] = iterableAsScalaIterable(headers).map(h => Header(h.getKey, h.getValue)).toList

}
