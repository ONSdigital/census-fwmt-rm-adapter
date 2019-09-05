package uk.gov.ons.census.fwmt.rmadapter.health;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.rmadapter.config.ActionFieldQueueConfig;
import uk.gov.ons.census.fwmt.rmadapter.config.GatewayActionsQueueConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class RabbitQueuesHealthIndicator extends AbstractHealthIndicator {

  private  List<String> queues;
  
  @Autowired
  @Qualifier("connectionFactory")
  private ConnectionFactory connectionFactory;
  
  private RabbitAdmin rabbitAdmin;

  
  public RabbitQueuesHealthIndicator(
      @Value("${rabbitmq.rmQueue}") String ACTION_FIELD_QUEUE,
      @Value("${rabbitmq.rmDeadLetter}") String ACTION_FIELD_DLQ) {
    queues = Arrays.asList(
        ACTION_FIELD_QUEUE,
        ACTION_FIELD_DLQ,
        GatewayActionsQueueConfig.GATEWAY_ACTIONS_QUEUE
    );  }

  
  private boolean checkQueue(String queueName) {
    Properties properties = rabbitAdmin.getQueueProperties(queueName);
    return (properties != null);
  }

  private Map<String, Boolean> getAccessibleQueues() {
    rabbitAdmin = new RabbitAdmin(connectionFactory);

    return queues.stream()
        .collect(Collectors.toMap(queueName -> queueName, this::checkQueue));
  }

  @Override protected void doHealthCheck(Health.Builder builder) {
    Map<String, Boolean> accessibleQueues = getAccessibleQueues();

    builder.withDetail("accessible-queues", accessibleQueues);

    if (accessibleQueues.containsValue(false)) {
      builder.down();
    } else {
      builder.up();
    }

  }
}
