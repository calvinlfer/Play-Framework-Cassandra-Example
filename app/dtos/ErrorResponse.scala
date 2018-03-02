package dtos

import play.api.libs.json._

case class ErrorResponse(errors: Map[String, String])

object ErrorResponse {
  implicit val errorResponseWrites: Writes[ErrorResponse] = Json.writes[ErrorResponse]
}
