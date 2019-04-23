package model

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, Reads, Writes}

case class Thing(name: String, age: Int, isCool: Boolean, nickName: Option[String])

object Thing {
  implicit val config = JsonConfiguration(SnakeCase)

  implicit val reads: Reads[Thing] = Json.reads[Thing]
  implicit val writes: Writes[Thing] = Json.writes[Thing]
}
