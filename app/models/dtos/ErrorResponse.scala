package models.dtos

import models.dtos.ErrorCode.ErrorCode
import play.api.libs.json.{JsValue, Json, Writes}

case class ErrorResponse(code: ErrorCode, errors: Seq[String])

object ErrorResponse {
  implicit val writes: Writes[ErrorResponse] = Json.writes[ErrorResponse]

  implicit class ErrorResponseOps(err: ErrorResponse) {
    def toJson: JsValue = Json.toJson(err)
  }

}

object ErrorCode extends Enumeration {
  type ErrorCode = Value

  val BadInput = Value("BadInput")
  val ServiceError = Value("ServiceError")
}