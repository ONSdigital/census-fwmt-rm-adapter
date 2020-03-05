package uk.gov.ons.census.fwmt.rmadapter.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import uk.gov.ons.census.fwmt.rmadapter.message.ActionInstructionReceiver;

@Configuration
public class ActionFieldQueueConfig {

  public String actionFieldQueueName;
  public String actionFieldDLQName;

  public ActionFieldQueueConfig(
      @Value("${rabbitmq.rmQueue}") String actionFieldQueueName,
      @Value("${rabbitmq.rmDeadLetter}") String actionFieldDLQName) {
    this.actionFieldQueueName = actionFieldQueueName;
    this.actionFieldDLQName = actionFieldDLQName;
  }

  @Autowired
  private AmqpAdmin rmAmqpAdmin;

  //Listener Adapter
  @Bean
  public MessageListenerAdapter actionFieldListenerAdapter(ActionInstructionReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  //Message Listener
  @Bean
  public SimpleMessageListenerContainer actionFieldMessengerListener(
      @Qualifier("rmConnectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("actionFieldListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    Advice[] adviceChain = {retryOperationsInterceptor};
    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(actionFieldQueueName);
    container.setMessageListener(messageListenerAdapter);
    container.setAmqpAdmin(rmAmqpAdmin);
    return container;
  }
}
