package repositories

import javax.inject.{Inject, Provider}

import scala.concurrent.Future

class PersonRepositoryProvider @Inject()(personDb: PersonDatabase) extends Provider[PersonRepository[Future]] {
  override def get(): PersonRepository[Future] = personDb.personRepo
}
