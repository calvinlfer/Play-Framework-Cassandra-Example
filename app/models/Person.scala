package models

import java.util.UUID

import models.Gender.Gender

object Gender extends Enumeration {
  type Gender = Value
  val Male, Female = Value
}

case class Person(id: UUID, firstName: String, lastName: String, gender: Gender)
