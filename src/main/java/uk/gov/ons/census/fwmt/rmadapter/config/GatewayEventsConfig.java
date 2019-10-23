package uk.gov.ons.census.fwmt.rmadapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.Application;

@Configuration
public class GatewayEventsConfig {

  public static final String CANONICAL_CREATE_SENT = "CANONICAL_CREATE_SENT";
  public static final String CANONICAL_UPDATE_SENT = "CANONICAL_UPDATE_SENT";
  public static final String CANONICAL_CANCEL_SENT = "CANONICAL_CANCEL_SENT";
  public static final String RM_CREATE_REQUEST_RECEIVED = "RM_CREATE_REQUEST_RECEIVED";
  public static final String RM_UPDATE_REQUEST_RECEIVED = "RM_UPDATE_REQUEST_RECEIVED";
  public static final String RM_CANCEL_REQUEST_RECEIVED = "RM_CANCEL_REQUEST_RECEIVED";
  public static final String RABBIT_QUEUE_UP = "RABBIT_QUEUE_UP";
  public static final String REDIS_SERVICE_UP = "REDIS_SERVICE_UP";

  public static final String INVALID_ACTION_INSTRUCTION = "INVALID_ACTION_INSTRUCTION";
  public static final String FAILED_TO_UNMARSHALL_ACTION_INSTRUCTION = "FAILED_TO_UNMARSHALL_ACTION_INSTRUCTION";
  public static final String FAILED_TO_MARSHALL_CANONICAL = "FAILED_TO_MARSHALL_CANONICAL";
  public static final String RABBIT_QUEUE_DOWN = "RABBIT_QUEUE_DOWN";
  public static final String REDIS_SERVICE_DOWN = "REDIS_SERVICE_DOWN";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.setSource(Application.APPLICATION_NAME);
    gatewayEventManager.addEventTypes(
        new String[] {CANONICAL_CREATE_SENT, CANONICAL_UPDATE_SENT, CANONICAL_CANCEL_SENT, RM_CREATE_REQUEST_RECEIVED,
            RM_UPDATE_REQUEST_RECEIVED, RM_CANCEL_REQUEST_RECEIVED, RABBIT_QUEUE_UP, REDIS_SERVICE_UP});
    gatewayEventManager.addErrorEventTypes(
        new String[] {INVALID_ACTION_INSTRUCTION, FAILED_TO_UNMARSHALL_ACTION_INSTRUCTION,
            FAILED_TO_MARSHALL_CANONICAL, RABBIT_QUEUE_DOWN, REDIS_SERVICE_DOWN});
    return gatewayEventManager;
  }
}
