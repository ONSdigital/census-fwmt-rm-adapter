package uk.gov.ons.fwmt.census.rmadapter.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

public final class ConnectionFactoryUtils {

  public static CachingConnectionFactory createConnectionFactory(int port, String hostname, String virtualHost,
      String password, String username) {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(hostname, port);

    cachingConnectionFactory.setVirtualHost(virtualHost);
    cachingConnectionFactory.setPassword(password);
    cachingConnectionFactory.setUsername(username);

    return cachingConnectionFactory;
  }
}
