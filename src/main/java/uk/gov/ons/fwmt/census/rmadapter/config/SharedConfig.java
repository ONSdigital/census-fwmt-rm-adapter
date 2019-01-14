package uk.gov.ons.fwmt.census.rmadapter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.ons.fwmt.census.rmadapter.retrysupport.DefaultListenerSupport;
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CTPRetryPolicy;

@Configuration
public class SharedConfig {

  private int initialInterval;
  private double multiplier;
  private int maxInterval;

  public SharedConfig(@Value("${rabbitmq.initialinterval}") Integer initialInterval,
      @Value("${rabbitmq.multiplier}") Double multiplier,
      @Value("${rabbitmq.maxInterval}") Integer maxInterval) {
    this.initialInterval = initialInterval;
    this.multiplier = multiplier;
    this.maxInterval = maxInterval;
  }

  // Retry Template
  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(initialInterval);
    backOffPolicy.setMultiplier(multiplier);
    backOffPolicy.setMaxInterval(maxInterval);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    CTPRetryPolicy ctpRetryPolicy = new CTPRetryPolicy();
    retryTemplate.setRetryPolicy(ctpRetryPolicy);

    retryTemplate.registerListener(new DefaultListenerSupport());

    return retryTemplate;
  }
}
