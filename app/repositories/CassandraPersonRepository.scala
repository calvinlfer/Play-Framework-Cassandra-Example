package repositories

import com.outworkers.phantom.dsl._
import ConsistencyLevel._
import models.Gender.Gender
import models.{Gender, Person}

import scala.concurrent.Future

abstract class CassandraPersonRepository
    extends Table[CassandraPersonRepository, Person]
    with PersonRepository[Future] {
  object id        extends UUIDColumn with PartitionKey
  object firstName extends StringColumn
  object lastName  extends StringColumn
  object gender    extends StringColumn

  implicit val genderPrimitive: Primitive[Gender] =
    Primitive.derive[Gender, String](_.toString)(string => Gender.withName(string))

  override def create(p: Person): Future[Person] =
    insert
      .value(_.id, p.id)
      .value(_.firstName, p.firstName)
      .value(_.lastName, p.lastName)
      .value(_.gender, p.gender.toString)
      .consistencyLevel_=(LOCAL_QUORUM)
      .future()
      .map(_ => p)

  override def find(id: UUID): Future[Option[Person]] =
    select
      .where(_.id eqs id)
      .consistencyLevel_=(LOCAL_QUORUM)
      .one()

  override def remove(id: UUID): Future[Unit] =
    delete
      .where(_.id eqs id)
      .consistencyLevel_=(LOCAL_QUORUM)
      .future()
      .map(_ => ())

}
