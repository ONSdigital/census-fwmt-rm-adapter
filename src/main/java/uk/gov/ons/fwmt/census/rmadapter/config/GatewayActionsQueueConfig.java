package uk.gov.ons.fwmt.census.rmadapter.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayActionsQueueConfig {

  @Autowired
  private AmqpAdmin amqpAdmin;
  
  public static final String GATEWAY_ACTIONS = "gateway.actions";
  public static final String GATEWAY_ACTIONS_DLQ = "gateway.actions.DLQ";

  public static final String GATEWAY_ACTIONS_EXCHANGE = "gateway.actions.exchange";
  public static final String GATEWAY_ACTIONS_ROUTING_KEY = "gateway.action.request";
  
  // Queue
  @Bean
  public Queue gatewayActionsQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_ACTIONS).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }
  
  //Exchange
  @Bean
  public DirectExchange gatewayActionsExchange() {
    DirectExchange directExchange = new DirectExchange(GATEWAY_ACTIONS_EXCHANGE);
    directExchange.setAdminsThatShouldDeclare(amqpAdmin);
    return directExchange;
  }

  // Bindings
  @Bean
  public Binding gatewayActionsBinding(@Qualifier("gatewayActionsQueue") Queue gatewayActionsQueue,
      @Qualifier("gatewayActionsExchange") DirectExchange gatewayActionsExchange) {
    Binding binding = BindingBuilder.bind(gatewayActionsQueue).to(gatewayActionsExchange)
        .with(GATEWAY_ACTIONS_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin);
    return binding;
  }
 
}
