package service

import javax.inject.Inject
import model.Thing
import persistence.ThingsRepository

import scala.concurrent.{ExecutionContext, Future}

class ThingsService @Inject()(thingsRepository: ThingsRepository) {

  def saveThing(t: Thing)(implicit ec: ExecutionContext): Future[Unit] = {
    thingsRepository.insert(t).map(_ => ())
  }

  def getThingsByAge(age: Int): Future[Seq[Thing]] = {
    thingsRepository.findByAge(age)
  }



}
