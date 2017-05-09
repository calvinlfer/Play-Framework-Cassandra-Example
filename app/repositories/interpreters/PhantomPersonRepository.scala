package repositories.interpreters


import java.util.UUID
import javax.inject.Inject

import cats.Monad
import com.outworkers.phantom.CassandraTable
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import models.Gender
import models.Person
import play.api.Configuration
import repositories.PersonRepository

import scala.concurrent.{ExecutionContext, Future}

class PhantomPersonRepository @Inject()(config: Configuration, connection: CassandraConnection, ec: ExecutionContext)
  extends CassandraTable[PhantomPersonRepository, Person] with PersonRepository[Future] {
  // See https://github.com/outworkers/phantom/wiki/Using-the-Database-class-and-understanding-connectors
  implicit val session: Session = connection.session
  implicit val keySpace: KeySpace = connection.provider.space
  override val tableName: String = config.getString("db.table").getOrElse("person_info")
  implicit val executionContext: ExecutionContext = ec

  object id extends UUIDColumn(this) with PartitionKey

  object firstName extends StringColumn(this) {
    override def name: String = "first_name"
  }

  object lastName extends StringColumn(this) {
    override def name: String = "last_name"
  }

  object studentId extends StringColumn(this) {
    override def name: String = "student_id"
  }

  object gender extends EnumColumn[Gender.Value](this)

  override implicit val monad: Monad[Future] = cats.instances.future.catsStdInstancesForFuture

  override def create(person: Person): Future[Person] =
    insert.value(_.id, person.id)
      .value(_.firstName, person.firstName)
      .value(_.lastName, person.lastName)
      .value(_.studentId, person.studentId)
      .value(_.gender, person.gender)
      .consistencyLevel_=(ConsistencyLevel.LOCAL_QUORUM)
      .future()
      .map(_ => person)

  // https://github.com/outworkers/phantom/wiki/Querying#query-api
  override def find(personId: UUID): Future[Option[Person]] =
    select.where(_.id eqs personId).one()

  // can be more fancy https://github.com/outworkers/phantom/wiki/Querying#update-queries
  override def update(person: Person): Future[Person] = create(person)

  override def deleteById(personId: UUID): Future[UUID] =
    delete.where(_.id eqs personId).future().map(_ => personId)

  override def findAll: Future[Seq[Person]] = select.all().fetch()
}