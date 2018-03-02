package repositories

import java.io.FileInputStream
import java.security.KeyStore

import com.datastax.driver.core._
import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.outworkers.phantom.database.Database
import javax.net.ssl.{SSLContext, TrustManagerFactory}

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy
import configuration.Settings

class PersonDatabase private[repositories] (override val connector: CassandraConnection)
    extends Database[PersonDatabase](connector) {
  object personRepo extends CassandraPersonRepository with connector.Connector
}

object PersonDatabase {
  def apply(settings: Settings): PersonDatabase = {
    val cassConfig = settings.cassandra

    def configureSSL(): Option[JdkSSLOptions] =
      if (cassConfig.trustStorePath.isEmpty) None
      else {
        // Set up trust-store
        val trustStore     = KeyStore.getInstance("JKS")
        val trustStoreFile = new FileInputStream(cassConfig.trustStorePath)
        trustStore.load(trustStoreFile, cassConfig.trustStorePass.toCharArray)
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
        tmf.init(trustStore)

        // Set up SSL context and feed in the trust-store
        val context = SSLContext.getInstance("TLS")
        context.init(null, tmf.getTrustManagers, null)
        Some(JdkSSLOptions.builder().withSSLContext(context).build())
      }

    def configurePlaintextAuth(): Option[AuthProvider] =
      for {
        user <- cassConfig.username
        pass <- cassConfig.password
      } yield new PlainTextAuthProvider(user, pass)

    lazy val connector: CassandraConnection =
      ContactPoints(hosts = cassConfig.host :: Nil, port = cassConfig.port)
        .withClusterBuilder { builder =>
          configureSSL().foreach(sslOptions => builder.withSSL(sslOptions))
          configurePlaintextAuth().foreach(authProvider => builder.withAuthProvider(authProvider))
          builder.withReconnectionPolicy(
            new ExponentialReconnectionPolicy(cassConfig.startReconnectTimeInMs, cassConfig.maxReconnectTimeInMs)
          )
          builder
        }
        .keySpace(name = cassConfig.keyspace, autoinit = cassConfig.autoInitKeyspace)

    new PersonDatabase(connector)
  }
}
