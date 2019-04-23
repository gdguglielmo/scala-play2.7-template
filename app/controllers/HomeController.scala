package controllers

import javax.inject._
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, Reads, Writes}
import play.api.mvc._
import util.{CustomWriteables, RequestHandlingExecutionContext}

import scala.concurrent.Future


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit ec: RequestHandlingExecutionContext)
  extends AbstractController(cc) with CustomWriteables {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def createThing = Action.async(parse.json[Thing]) { implicit request =>
//    as[Thing](request) { t: Thing =>
      Future(Ok(request.body))
//    }
  }

  case class Thing(name: String, age: Int, isCool: Boolean, nickName: Option[String])
  object Thing {

    implicit val config = JsonConfiguration(SnakeCase)
//    implicit val format: Format[Thing] = Json.format[Thing]
    implicit val reads: Reads[Thing] = Json.reads[Thing]
    implicit val writes: Writes[Thing] = Json.writes[Thing]
  }

}
