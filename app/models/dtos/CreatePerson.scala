package models.dtos

import models.Person
import models.dtos.Gender.Gender
import play.api.libs.json.{Format, Json, Reads, Writes}

object Gender extends Enumeration {
  type Gender = Value
  val Male, Female = Value

  implicit val enumReads: Format[Gender] = EnumerationJsonFormat.enumFormat(Gender)

  implicit class GenderModelOps(dto: Gender) {
    def toModel: models.Gender.Gender = models.Gender.withName(dto.toString)
  }
}

case class CreatePerson(firstName: String, lastName: String, studentId: String, gender: Gender)

object CreatePerson {
  implicit val createPersonReads: Reads[CreatePerson] = Reads[CreatePerson] { json =>
    for {
      firstName <- (json \ "firstName").validate[String]
      lastName <- (json \ "lastName").validate[String]
      studentId <- (json \ "studentId").validate[String]
      gender <- (json \ "gender").validate[Gender]
    } yield CreatePerson(firstName, lastName, studentId, gender)
  }
}

case class PersonsResult(results: Seq[Person], nextPage: Option[String])

object PersonsResult {
  implicit val personResultWrites: Writes[PersonsResult] = Json.writes[PersonsResult]
}
