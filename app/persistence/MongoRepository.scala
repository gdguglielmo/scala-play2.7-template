package persistence

import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.{Completed, MongoCollection}

import scala.concurrent.Future
import scala.reflect.ClassTag

abstract class MongoRepository[A: ClassTag] {

  def collectionName: String
  def mongoClientProvider: MongoClientProvider

  val collection: MongoCollection[A] = mongoClientProvider.database.getCollection[A](collectionName)

  def find(query: Bson): Future[Seq[A]] = {
    collection.find(query).toFuture
  }

  def findOne(query: Bson): Future[A] = {
    collection.find(query).first().toFuture
  }

  def insert(elem: A): Future[Completed] = {
    collection.insertOne(elem).toFuture
  }



}
