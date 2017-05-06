package models

import java.util.UUID

import models.Gender.Gender

case class Person(id: UUID, firstName: String, lastName: String, studentId: String, gender: Gender)

object Gender extends Enumeration {
  type Gender = Value
  val Male, Female = Value
}
