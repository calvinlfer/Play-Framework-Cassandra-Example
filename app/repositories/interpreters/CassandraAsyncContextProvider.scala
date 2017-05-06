package repositories.interpreters

import javax.inject.Provider

import io.getquill.{CassandraAsyncContext, SnakeCase}

class CassandraAsyncContextProvider extends Provider[CassandraAsyncContext[SnakeCase]] {
  override def get(): CassandraAsyncContext[SnakeCase] =
    // read from the db object in application.conf
    new CassandraAsyncContext[SnakeCase]("db")
}
