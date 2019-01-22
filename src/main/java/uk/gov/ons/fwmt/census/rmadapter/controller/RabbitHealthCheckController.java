package uk.gov.ons.fwmt.census.rmadapter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/rabbitHealth")
public class RabbitHealthCheckController {

  @Autowired
  @Qualifier("rmConnectionFactory")
  private ConnectionFactory rmFactory;

  @Autowired
  @Qualifier("fwmtConnectionFactory")
  private ConnectionFactory fwmtConnectionFactory;

  private RabbitAdmin rmRabbitAdmin;
  private RabbitAdmin fwmtRabbitAdmin;

  private String checkQueue(String queueName) {
    RabbitAdmin rabbitAdmin = ("Action.Field".equals(queueName) || "Action.FieldDLQ".equals(queueName)) ?
        rmRabbitAdmin :
        fwmtRabbitAdmin;
    Properties props = rabbitAdmin.getQueueProperties(queueName);
    return (props != null) ? props.getProperty("QUEUE_NAME") : null;
  }

  @RequestMapping(value = "/rabbitHealth", method = RequestMethod.GET, produces = "application/json")
  public List<String> canAccessQueue() {
    rmRabbitAdmin = new RabbitAdmin(rmFactory);
    fwmtRabbitAdmin = new RabbitAdmin(fwmtConnectionFactory);
    List<String> queues = Arrays.asList(
        "gateway.feedback",
        "gateway.feedback.DLQ",
        "gateway.actions",
        "gateway.actions.DLQ",
        "Action.Field",
        "Action.FieldDLQ",
        "rm.feedback",
        "rm.feedback.DLQ"
    );

      return queues.stream()
          .map(a -> this.checkQueue(a))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
}

}
