package controllers

import javax.inject._
import model.Thing
import play.api.mvc._
import util.{CustomWriteables, RequestHandlingExecutionContext}

import scala.concurrent.Future

@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit ec: RequestHandlingExecutionContext)
  extends AbstractController(cc) with CustomWriteables {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def createThing: Action[Thing] = Action.async(parse.json[Thing]) { implicit request =>
    Future(Ok(request.body))
  }

}
