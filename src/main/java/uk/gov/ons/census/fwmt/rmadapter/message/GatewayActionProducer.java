package uk.gov.ons.census.fwmt.rmadapter.message;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.config.GatewayActionsQueueConfig;
import uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig;

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
    rabbitTemplate.convertAndSend(gatewayActionsExchange.getName(),
        GatewayActionsQueueConfig.GATEWAY_ACTIONS_ROUTING_KEY, JSONJobRequest);
  }

  protected String convertToJSON(Object dto) throws GatewayException {
    String JSONJobRequest;
    try {
      JSONJobRequest = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      String msg = "Failed to convert to JSON.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), e, msg, getCaseId(dto), GatewayEventsConfig.FAILED_TO_MARSHALL_CANONICAL);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, ".", e);
    }
    return JSONJobRequest;
  }

  private String getCaseId(Object dto) {
    String caseId = "";
    if (dto instanceof CreateFieldWorkerJobRequest) {
      caseId = ((CreateFieldWorkerJobRequest) dto).getCaseId().toString();
    } else if (dto instanceof CancelFieldWorkerJobRequest) {
      caseId = ((CreateFieldWorkerJobRequest) dto).getCaseId().toString();
    } else if (dto instanceof UpdateFieldWorkerJobRequest) {
      caseId = ((CreateFieldWorkerJobRequest) dto).getCaseId().toString();
    }
    return caseId;
  }
}
