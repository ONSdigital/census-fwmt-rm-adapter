package uk.gov.ons.census.fwmt.rmadapter.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.config.GatewayActionsQueueConfig;
import uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig;

import java.util.UUID;

@Slf4j
@Component
public class GatewayActionProducer {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  @Qualifier("gatewayActionsExchange")
  private DirectExchange gatewayActionsExchange;

  @Autowired
  private ObjectMapper objectMapper;

  @Retryable
  public void sendMessage(Object dto) throws GatewayException {
    String JSONJobRequest = convertToJSON(dto);

    Message gatewayMessage = convertJSONToMessage(JSONJobRequest);

    rabbitTemplate.convertAndSend(gatewayActionsExchange.getName(),
        GatewayActionsQueueConfig.GATEWAY_ACTIONS_ROUTING_KEY, gatewayMessage);
  }

  protected String convertToJSON(Object dto) throws GatewayException {
    String JSONJobRequest;
    try {
      JSONJobRequest = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      String msg = "Failed to convert to JSON.";
      gatewayEventManager
          .triggerErrorEvent(this.getClass(), e, msg, getCaseId(dto), GatewayEventsConfig.FAILED_TO_MARSHALL_CANONICAL);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, ".", e);
    }
    return JSONJobRequest;
  }

  private String getCaseId(Object dto) {
    String caseId = "<NULL>";
    UUID uuid = null;
    if (dto instanceof CreateFieldWorkerJobRequest) {
      uuid = ((CreateFieldWorkerJobRequest) dto).getCaseId();
    } else if (dto instanceof CancelFieldWorkerJobRequest) {
      uuid = ((CancelFieldWorkerJobRequest) dto).getCaseId();
    } else if (dto instanceof UpdateFieldWorkerJobRequest) {
      uuid = ((UpdateFieldWorkerJobRequest) dto).getCaseId();
    }
    if (uuid != null)
      caseId = uuid.toString();
    return caseId;
  }

  public Message convertJSONToMessage(String messageJSON) {
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();

    return messageConverter.toMessage(messageJSON, messageProperties);
  }
}
