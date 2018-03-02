package repositories

import java.util.UUID

import models.Person

import scala.language.higherKinds

trait PersonRepository[F[_]] {
  def create(p: Person): F[Person]
  def find(id: UUID): F[Option[Person]]
  def remove(id: UUID): F[Unit]
}
