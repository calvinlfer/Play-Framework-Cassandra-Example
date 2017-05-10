package repositories

import java.util.UUID

import cats.Monad
import models.Person

import scala.language.higherKinds

case class Page[A](result: A, next: Option[String])

trait PersonRepository[Effect[_]] {
  implicit val monad: Monad[Effect]

  def create(person: Person): Effect[Person]

  def find(personId: UUID): Effect[Option[Person]]

  def update(person: Person): Effect[Person]

  def deleteById(personId: UUID): Effect[UUID]

  def findAll(pagingState: Option[String]): Effect[Page[Seq[Person]]]
}
