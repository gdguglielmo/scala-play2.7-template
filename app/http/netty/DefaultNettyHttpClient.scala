package http.netty

import javax.inject.Inject
import org.asynchttpclient.AsyncHttpClient

class DefaultNettyHttpClient @Inject()(protected val innerAsyncHttpClient: AsyncHttpClient) extends NettyHttpClient
