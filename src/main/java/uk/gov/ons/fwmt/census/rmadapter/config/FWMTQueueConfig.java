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
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CustomMessageRecover;

import static uk.gov.ons.fwmt.census.rmadapter.config.ConnectionFactoryUtils.createConnectionFactory;

@Configuration
public class FWMTQueueConfig {

  private static final String JOBSVC_TO_ADAPTER_QUEUE = "gateway.feedback";
  private static final String ADAPTER_TO_JOBSVC_QUEUE = "gateway.actions";
  private static final String ADAPTER_TO_RM_QUEUE = "rm.feedback";
  private static final String JOBSVC_TO_ADAPTER_DLQ = JOBSVC_TO_ADAPTER_QUEUE + ".DLQ";
  private static final String ADAPTER_TO_JOBSVC_DLQ = ADAPTER_TO_JOBSVC_QUEUE + ".DLQ";
  private static final String ADAPTER_TO_RM_DLQ = ADAPTER_TO_RM_QUEUE + ".DLQ";
  private static final String RM_JOB_SVC_EXCHANGE = "gateway.feedback.exchange";
  private static final String JOB_SVC_RESPONSE_ROUTING_KEY = "jobsvc.job.response";
  private static final String JOB_SVC_REQUEST_ROUTING_KEY = "jobsvc.job.request";
  private static final String RM_RESPONSE_ROUTING_KEY = "rm.job.response";

  private String username;
  private String password;
  private String hostname;
  private int fwmtPort;
  private String virtualHost;

  public FWMTQueueConfig(
      @Value("${rabbitmq.fwmt.username}") String username,
      @Value("${rabbitmq.fwmt.password}") String password,
      @Value("${rabbitmq.fwmt.hostname}") String hostname,
      @Value("${rabbitmq.fwmt.port}") Integer fwmtPort,
      @Value("${rabbitmq.fwmt.virtualHost}") String virtualHost) {
    this.username = username;
    this.password = password;
    this.hostname = hostname;
    this.fwmtPort = fwmtPort;
    this.virtualHost = virtualHost;
  }

  // Queue
  @Bean
  public Queue adapterToJobSvcQueue() {
    Queue queue = QueueBuilder.durable(ADAPTER_TO_JOBSVC_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", ADAPTER_TO_JOBSVC_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return queue;
  }

  @Bean
  public Queue jobSvcToAdapterQueue() {
    Queue queue = QueueBuilder.durable(JOBSVC_TO_ADAPTER_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", JOBSVC_TO_ADAPTER_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return queue;
  }

  @Bean
  public Queue adapterToRmQueue() {
    Queue queue = QueueBuilder.durable(ADAPTER_TO_RM_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", ADAPTER_TO_RM_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return queue;
  }

  // Dead Letter Queues
  @Bean
  public Queue rmAdapterJobDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(ADAPTER_TO_JOBSVC_DLQ).build();
    queue.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return queue;
  }

  @Bean
  public Queue jobSvsDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(JOBSVC_TO_ADAPTER_DLQ).build();
    queue.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return queue;
  }

  @Bean
  public Queue adapterRmDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(ADAPTER_TO_RM_DLQ).build();
    queue.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return queue;
  }

  // Exchange
  @Bean
  @Primary
  public DirectExchange fwmtExchange() {
    DirectExchange directExchange = new DirectExchange(RM_JOB_SVC_EXCHANGE);
    directExchange.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return directExchange;
  }

  // Bindings
  @Bean
  public Binding adapterToJobSvcBinding(@Qualifier("adapterToJobSvcQueue") Queue queue,
      @Qualifier("fwmtExchange") DirectExchange directExchange) {
    Binding binding = BindingBuilder.bind(queue).to(directExchange)
        .with(JOB_SVC_REQUEST_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return binding;
  }

  @Bean
  public Binding jobSvcToAdapterBinding(@Qualifier("jobSvcToAdapterQueue") Queue queue,
      @Qualifier("fwmtExchange") DirectExchange directExchange) {
    Binding binding = BindingBuilder.bind(queue).to(directExchange)
        .with(JOB_SVC_RESPONSE_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return binding;
  }

  @Bean
  public Binding adapterToRmBinding(@Qualifier("adapterToRmQueue") Queue queue,
      @Qualifier("fwmtExchange") DirectExchange directExchange) {
    Binding binding = BindingBuilder.bind(queue).to(directExchange)
        .with(RM_RESPONSE_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(fwmtAmqpAdmin());
    return binding;
  }

  // Listener
  @Bean
  public MessageListenerAdapter jobSvcListenerAdapter(JobServiceReceiverImpl receiver) {
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
      @Qualifier("fwmtConnectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("jobSvcListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    Advice[] adviceChain = {retryOperationsInterceptor};

    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(JOBSVC_TO_ADAPTER_QUEUE);
    container.setMessageListener(messageListenerAdapter);
    return container;
  }

  @Bean
  public AmqpAdmin fwmtAmqpAdmin() {
    return new RabbitAdmin(fwmtConnectionFactory());
  }

  // Connection Factory
  @Bean
  @Primary
  public ConnectionFactory fwmtConnectionFactory() {
    return createConnectionFactory(fwmtPort, hostname, virtualHost, password, username);
  }

}
