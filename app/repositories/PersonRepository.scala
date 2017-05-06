package repositories

import java.util.UUID

import cats.Monad
import models.Person

import scala.language.higherKinds

trait PersonRepository[Effect[_]] {
  implicit val monad: Monad[Effect]

  def create(person: Person): Effect[Person]

  def find(personId: UUID): Effect[Option[Person]]

  def update(person: Person): Effect[Person]

  def delete(personId: UUID): Effect[UUID]
}
