package configuration

import javax.inject.Inject

import play.api.Configuration

class Settings @Inject()(config: Configuration) {
  object cassandra {
    val host: String                 = config.get[String]("app.cassandra.host")
    val port: Int                    = config.get[Int]("app.cassandra.port")
    val startReconnectTimeInMs: Long = config.get[Long]("app.cassandra.start-reconnect-ms")
    val maxReconnectTimeInMs: Long   = config.get[Long]("app.cassandra.max-reconnect-ms")
    val keyspace: String             = config.get[String]("app.cassandra.keyspace")
    val trustStorePath: String       = config.get[String]("app.cassandra.truststore-path")
    val trustStorePass: String       = config.get[String]("app.cassandra.truststore-password")
    val username: Option[String] = {
      val user = config.get[String]("app.cassandra.username")
      if (user.nonEmpty) Some(user) else None
    }
    val password: Option[String] = {
      val pass = config.get[String]("app.cassandra.password")
      if (pass.nonEmpty) Some(pass) else None
    }
    val autoInitKeyspace: Boolean = config.get[Boolean]("app.cassandra.initialize-keyspace")
  }
}
