package repositories.interpreters

import java.util.UUID
import javax.inject.Inject

import cats.Monad
import io.getquill._
import models.Gender.Gender
import models.{Gender, Person}
import repositories.PersonRepository

import scala.concurrent.{ExecutionContext, Future}

class QuillPersonRepository @Inject() (cassandra: CassandraAsyncContext[SnakeCase])(implicit ec: ExecutionContext)
  extends PersonRepository[Future] {
  import cassandra._

  private implicit val encodeGender: MappedEncoding[Gender, String] = MappedEncoding[Gender, String](_.toString)
  private implicit val decodeGender: MappedEncoding[String, Gender] = MappedEncoding[String, Gender](Gender.withName)
  
  override implicit val monad: Monad[Future] = cats.instances.future.catsStdInstancesForFuture

  override def create(person: Person): Future[Person] = {
    val insertInstruction = quote { (person: Person) => querySchema[Person]("person_info").insert(person) }
    cassandra.run(insertInstruction(lift(person))).map(_ => person)
  }

  override def find(personId: UUID): Future[Option[Person]] = {
    val findInstruction = quote { (id: UUID) => querySchema[Person]("person_info").filter(_.id == id) }
    cassandra.run(findInstruction(lift(personId))).map(_.headOption)
  }

  override def update(person: Person): Future[Person] = create(person)

  override def deleteById(personId: UUID): Future[UUID] = {
    val deleteInstruction =  quote { (id: UUID) => querySchema[Person]("person_info").filter(_.id == id).delete }
    cassandra.run(deleteInstruction(lift(personId))).map(_ => personId)
  }

  override def findAll: Future[Seq[Person]] = {
    val findAllInstruction = quote { querySchema[Person]("person_info")}
    cassandra.run(findAllInstruction)
  }
}
