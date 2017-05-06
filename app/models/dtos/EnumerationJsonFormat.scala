package models.dtos

import play.api.libs.json._

import scala.language.implicitConversions

object EnumerationJsonFormat {
  def enumReads[E <: Enumeration](enum: E): Reads[E # Value] = new Reads[E # Value] {
    def reads(json: JsValue): JsResult[E # Value] = json match {
      case JsString(input) =>
        try {
          JsSuccess(enum.withName(input))
        } catch {
          case _: NoSuchElementException => JsError(s"Expected values: (${enum.values.mkString(", ")}) but you provided: '$input'")
        }
      case _ => JsError("String value expected")
    }
  }

  implicit def enumWrites[E <: Enumeration]: Writes[E # Value] = new Writes[E # Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }

  implicit def enumFormat[E <: Enumeration](enum: E): Format[E # Value] = {
    Format(enumReads(enum), enumWrites)
  }
}