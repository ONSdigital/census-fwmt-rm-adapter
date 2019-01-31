package uk.gov.ons.fwmt.census.rmadapter.message;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.rmadapter.config.QueueConfig;

@Slf4j
@Component
public class JobServiceProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private Exchange exchange;

  @Autowired
  private ObjectMapper objectMapper;

  @Retryable
  public void sendMessage(Object dto) throws GatewayException {
    String JSONJobRequest = convertToJSON(dto);
    rabbitTemplate.convertAndSend(exchange.getName(), QueueConfig.JOBSVC_REQUEST_ROUTING_KEY, JSONJobRequest);
    log.info("Message send to queue");
  }

  protected String convertToJSON(Object dto) throws GatewayException {
    String JSONJobRequest;
    try {
      JSONJobRequest = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to process JSON.", e);
    }
    return JSONJobRequest;
  }
}
