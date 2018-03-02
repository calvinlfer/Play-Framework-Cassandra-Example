package repositories

import javax.inject.{Inject, Provider}
import scala.concurrent.duration._
import configuration.Settings

class PersonDatabaseProvider @Inject()(settings: Settings) extends Provider[PersonDatabase] {
  override def get(): PersonDatabase = {
    import com.outworkers.phantom.dsl._
    val db = PersonDatabase(settings)
    db.create(10.seconds)(scala.concurrent.ExecutionContext.global) // WARNING: don't auto-create table in production
    db
  }
}
