package modules

import com.google.inject.Scopes.SINGLETON
import com.google.inject.{AbstractModule, TypeLiteral}
import repositories.{PersonDatabase, PersonDatabaseProvider, PersonRepository, PersonRepositoryProvider}
import services.{PersonService, PersonServiceProvider}

import scala.concurrent.Future

class AppConfigurationModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[PersonDatabase]).toProvider(classOf[PersonDatabaseProvider]).in(SINGLETON)
    bind(new TypeLiteral[PersonRepository[Future]]() {}).toProvider(classOf[PersonRepositoryProvider]).in(SINGLETON)
    bind(new TypeLiteral[PersonService[Future]]()    {}).toProvider(classOf[PersonServiceProvider]).in(SINGLETON)
  }
}
