package model

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, Reads, Writes}

case class Thing(name: String, age: Int, isCool: Boolean, nickName: Option[String])
case class Thing2(name: String, age: Int, isCool: Boolean, nickName: String, asd: Int)

object Thing {
  implicit val config = JsonConfiguration(SnakeCase)

  implicit val reads: Reads[Thing] = Json.reads[Thing]
  implicit val writes: Writes[Thing] = Json.writes[Thing]
}

object Thing2 {
  implicit val config = JsonConfiguration(SnakeCase)

  implicit val reads: Reads[Thing2] = Json.reads[Thing2]
  implicit val writes: Writes[Thing2] = Json.writes[Thing2]
}
