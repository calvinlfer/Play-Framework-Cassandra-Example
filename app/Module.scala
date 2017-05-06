import javax.inject.Singleton
import com.google.inject.{AbstractModule, TypeLiteral}
import io.getquill.{CassandraAsyncContext, SnakeCase}
import net.codingwell.scalaguice.ScalaModule
import repositories.PersonRepository
import repositories.interpreters.{CassandraAsyncContextProvider, QuillPersonRepository}

import scala.concurrent.Future

class Module extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CassandraAsyncContext[SnakeCase]].toProvider[CassandraAsyncContextProvider].in[Singleton]
    bind(new TypeLiteral[PersonRepository[Future]]{}).to(classOf[QuillPersonRepository])
  }
}
