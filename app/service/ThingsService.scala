package service

import http.{Ok, ThingClientException, ThingsClient}
import javax.inject.Inject
import model.{Thing, Thing2}
import persistence.ThingsRepository

import scala.concurrent.{ExecutionContext, Future}

class ThingsService @Inject()(thingsRepository: ThingsRepository, thingsClient: ThingsClient) {

  def saveThing(t: Thing)(implicit ec: ExecutionContext): Future[Unit] = {
    thingsRepository.insert(t).map(_ => ())
  }

  def getThingsByAge(age: Int): Future[Seq[Thing]] = {
    thingsRepository.findByAge(age)
  }

  def getThings42(implicit ec: ExecutionContext): Future[Seq[Thing2]] = {
    thingsClient.findThingAge42.map {
      case response: Ok[Seq[Thing2]] => response.content
    } .recoverWith({ case t => Future.failed(new ThingClientException("An error occurred while called while calling me", t)) })
  }



}
