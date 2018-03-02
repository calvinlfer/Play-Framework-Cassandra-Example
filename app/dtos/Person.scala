package dtos

import java.util.UUID

import dtos.Gender.Gender
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Gender extends Enumeration {
  type Gender = Value
  val Male, Female = Value
}

case class Person(firstName: String, lastName: String, gender: Gender)

object Person extends EnumerationJsonFormat {
  implicit class DTOPersonOps(p: dtos.Person) {
    def toModel(id: UUID): models.Person =
      models.Person(id, p.firstName, p.lastName, models.Gender.withName(p.gender.toString))
  }

  private implicit val genderFormat: Format[dtos.Gender.Value] = enumFormat(Gender)

  // Use error-accumulating semantics for validation
  implicit val createPersonReads: Reads[Person] = {
    val mustNotBeEmpty = "Provided string must not be empty"
    (
      (__ \ "firstName").read[String].filter(JsonValidationError(mustNotBeEmpty))(_.nonEmpty) ~
      (__ \ "lastName").read[String].filter(JsonValidationError(mustNotBeEmpty))(_.nonEmpty) ~
      (__ \ "gender").read[Gender]
    )(Person.apply _)
  }
}
