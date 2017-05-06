package models.dtos

import models.dtos.Gender.Gender
import play.api.libs.json.{Format, Reads}

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
      lastName  <- (json \ "lastName").validate[String]
      studentId <- (json \ "studentId").validate[String]
      gender    <- (json \ "gender").validate[Gender]
    } yield CreatePerson(firstName, lastName, studentId, gender)
  }
}