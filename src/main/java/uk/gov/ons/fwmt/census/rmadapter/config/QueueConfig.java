package uk.gov.ons.fwmt.census.rmadapter.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import uk.gov.ons.fwmt.census.rmadapter.message.impl.JobServiceReceiverImpl;
import uk.gov.ons.fwmt.census.rmadapter.message.impl.RMReceiverImpl;
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CustomMessageRecover;

import static uk.gov.ons.fwmt.census.rmadapter.config.ConnectionFactoryUtils.createConnectionFactory;

@Configuration
public class QueueConfig {
  
  public static final String GATEWAY_FEEDBACK = "gateway.feedback";
  public static final String GATEWAY_ACTIONS = "gateway.actions";
  public static final String RM_FEEDBACK = "rm.feedback";
  public static final String GATEWAY_FEEDBACK_DLQ = "gateway.feedback.DLQ";
  public static final String GATEWAY_ACTIONS_DLQ = "gateway.actions.DLQ";
  public static final String RM_FEEDBACK_DLQ = "rm.feedback.DLQ";
  public static final String GATEWAY_FEEDBACK_EXCHANGE = "gateway.feedback.exchange";
  public static final String JOBSVC_JOB_RESPONSE_ROUTING_KEY = "jobsvc.job.response";
  public static final String JOBSVC_REQUEST_ROUTING_KEY = "jobsvc.job.request";
  public static final String RM_RESPONSE_ROUTING_KEY = "rm.job.response";
  
  public static final String ACTION_FIELD_DLQ = "Action.FieldDLQ";
  public static final String ACTION_FIELD_QUEUE = "Action.Field";
  public static final String ACTION_FIELD_BINDING = "Action.Field.binding";
  public static final String ACTION_DEADLETTER_EXCHANGE = "action-deadletter-exchange";

  private String username;
  private String password;
  private String hostname;
  private int port;
  private String virtualHost;

  public QueueConfig(
      @Value("${rabbitmq.username}") String username,
      @Value("${rabbitmq.password}") String password,
      @Value("${rabbitmq.hostname}") String hostname,
      @Value("${rabbitmq.port}") Integer port,
      @Value("${rabbitmq.virtualHost}") String virtualHost) {
    this.username = username;
    this.password = password;
    this.hostname = hostname;
    this.port = port;
    this.virtualHost = virtualHost;
  }

  // Queue
  @Bean
  public Queue gatewayActionsQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_ACTIONS)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", GATEWAY_ACTIONS_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  @Bean
  public Queue gatewayFeedbackQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_FEEDBACK)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", GATEWAY_FEEDBACK_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  @Bean
  public Queue rmFeedbackQueue() {
    Queue queue = QueueBuilder.durable(RM_FEEDBACK)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", RM_FEEDBACK_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  @Bean
  public Queue actionFieldQueue() {
    Queue queue = QueueBuilder.durable(ACTION_FIELD_QUEUE)
        .withArgument("x-dead-letter-exchange", ACTION_DEADLETTER_EXCHANGE)
        .withArgument("x-dead-letter-routing-key", ACTION_FIELD_BINDING)
        .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  
  // Dead Letter Queues
  @Bean
  public Queue gatewayActionsDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_ACTIONS_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  @Bean
  public Queue gatewayFeedbackDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_FEEDBACK_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  @Bean
  public Queue rmFeedbackDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(RM_FEEDBACK_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  @Bean
  public Queue actionFieldDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(ACTION_FIELD_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin());
    return queue;
  }

  // Exchange
  @Bean
  @Primary
  public DirectExchange gatewayFeedbackExchange() {
    DirectExchange directExchange = new DirectExchange(GATEWAY_FEEDBACK_EXCHANGE);
    directExchange.setAdminsThatShouldDeclare(amqpAdmin());
    return directExchange;
  }
  
  @Bean
  public DirectExchange actionFieldDlqExchange() {
    DirectExchange exchange = new DirectExchange(ACTION_DEADLETTER_EXCHANGE);
    exchange.setAdminsThatShouldDeclare(amqpAdmin());
    return exchange;
  }

  // Bindings
  @Bean
  public Binding gatewayActionsBinding(@Qualifier("gatewayActionsQueue") Queue queue,
      @Qualifier("gatewayFeedbackExchange") DirectExchange directExchange) {
    Binding binding = BindingBuilder.bind(queue).to(directExchange)
        .with(JOBSVC_REQUEST_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin());
    return binding;
  }

  @Bean
  public Binding gatewayFeedbackBinding(@Qualifier("gatewayFeedbackQueue") Queue queue,
      @Qualifier("gatewayFeedbackExchange") DirectExchange directExchange) {
    Binding binding = BindingBuilder.bind(queue).to(directExchange)
        .with(JOBSVC_JOB_RESPONSE_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin());
    return binding;
  }

  @Bean
  public Binding rmFeedbackBinding(@Qualifier("rmFeedbackQueue") Queue queue,
      @Qualifier("gatewayFeedbackExchange") DirectExchange directExchange) {
    Binding binding = BindingBuilder.bind(queue).to(directExchange)
        .with(RM_RESPONSE_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin());
    return binding;
  }

  @Bean
  public Binding actionFieldBinding() {
    Binding binding = BindingBuilder.bind(actionFieldDeadLetterQueue()).to(actionFieldDlqExchange())
        .with(ACTION_FIELD_BINDING);
    binding.setAdminsThatShouldDeclare(amqpAdmin());
    return binding;
  }
  
  // Listener
  @Bean
  public MessageListenerAdapter jobSvcListenerAdapter(JobServiceReceiverImpl receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  public MessageListenerAdapter rmListenerAdapter(RMReceiverImpl receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  
  // Interceptor
  @Bean
  public RetryOperationsInterceptor interceptor(
      @Qualifier("retryTemplate") RetryOperations retryOperations) {
    RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
    interceptor.setRecoverer(new CustomMessageRecover());
    interceptor.setRetryOperations(retryOperations);
    return interceptor;
  }

  // Container
  @Bean
  public SimpleMessageListenerContainer jobSvcContainer(
      @Qualifier("connectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("jobSvcListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    Advice[] adviceChain = {retryOperationsInterceptor};

    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(GATEWAY_FEEDBACK);
    container.setMessageListener(messageListenerAdapter);
    return container;
  }

  // Container
  @Bean
  public SimpleMessageListenerContainer rmContainer(
      @Qualifier("connectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("rmListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    Advice[] adviceChain = {retryOperationsInterceptor};

    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(ACTION_FIELD_QUEUE);
    container.setMessageListener(messageListenerAdapter);
    return container;
  }

  @Bean
  public AmqpAdmin amqpAdmin() {
    return new RabbitAdmin(connectionFactory());
  }

  // Connection Factory
  @Bean
  @Primary
  public ConnectionFactory connectionFactory() {
    return createConnectionFactory(port, hostname, virtualHost, password, username);
  }

}
