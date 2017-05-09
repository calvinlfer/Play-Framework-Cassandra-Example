package repositories.interpreters

import javax.inject.{Inject, Provider}

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import play.Configuration
import scala.collection.JavaConverters._

class CassandraConnectionProvider @Inject()(config: Configuration) extends Provider[CassandraConnection] {
  override def get(): CassandraConnection = {
    val hosts = config.getStringList("db.session.contactPoints")
    val keyspace = config.getString("db.keyspace")

    // Use the Cluster Builder if you need to add username/password and handle SSL or tweak the connection
    ContactPoints(hosts.asScala).keySpace(keyspace)
  }
}
