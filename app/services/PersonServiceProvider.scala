package services

import javax.inject.Inject

import cats.Monad
import com.google.inject.Provider
import repositories.PersonRepository

import scala.concurrent.{ExecutionContext, Future}

class PersonServiceProvider @Inject()(personRepository: PersonRepository[Future], ec: ExecutionContext)
    extends Provider[PersonService[Future]] {
  override def get(): PersonService[Future] = {
    implicit val monadFuture: Monad[Future] = cats.instances.future.catsStdInstancesForFuture(ec)
    new PersonService[Future](personRepository)
  }
}
