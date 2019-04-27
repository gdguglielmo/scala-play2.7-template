package http

import com.google.inject.AbstractModule
import http.netty.NettyHttpClient
import org.asynchttpclient.AsyncHttpClient
import play.api.{Configuration, Environment}

class HttpModule(environment: Environment,
                 configuration: Configuration) extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[AsyncHttpClient]).toInstance(NettyHttpClient.asyncHttpClient(configuration))
  }

}
