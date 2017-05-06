package models

import java.util.UUID

import models.Gender.Gender
import models.dtos.EnumerationJsonFormat
import play.api.libs.json.{Format, Json, Writes}

case class Person(id: UUID, firstName: String, lastName: String, studentId: String, gender: Gender)

object Person {
  implicit val jsonWrites: Writes[Person] = Json.writes[Person]
}

object Gender extends Enumeration {
  type Gender = Value
  val Male, Female = Value

  implicit val genderWrites: Format[models.Gender.Value] = EnumerationJsonFormat.enumFormat(Gender)
}
