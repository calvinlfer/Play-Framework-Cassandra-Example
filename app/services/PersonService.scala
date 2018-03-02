package services

import java.util.UUID

import cats.Monad
import models.Person
import repositories.PersonRepository
import cats.syntax.all._

import scala.language.higherKinds

sealed trait PersonError
case class PersonAlreadyExists(id: UUID) extends PersonError

class PersonService[F[_]: Monad](personRepository: PersonRepository[F]) {
  def find(id: UUID): F[Option[Person]] = personRepository.find(id)

  def create(person: Person): F[Either[PersonError, Person]] =
    personRepository.find(person.id).flatMap {
      case Some(_) => Monad[F].pure(PersonAlreadyExists(person.id).asLeft)
      case None    => personRepository.create(person).map(_.asRight)
    }

  def remove(id: UUID): F[Option[Person]] =
    personRepository.find(id).flatMap {
      case None           => Monad[F].pure(None)
      case Some(existing) => personRepository.remove(id).map(_ => Some(existing))
    }
}
