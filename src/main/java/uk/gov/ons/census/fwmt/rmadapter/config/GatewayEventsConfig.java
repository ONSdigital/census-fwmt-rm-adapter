package uk.gov.ons.census.fwmt.rmadapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.Application;

@Configuration
public class GatewayEventsConfig {

  public static final String CANONICAL_CREATE_SENT = "Canonical - Action Create Sent";
  public static final String CANONICAL_UPDATE_SENT = "Canonical - Action Update Sent";
  public static final String CANONICAL_CANCEL_SENT = "Canonical - Action Cancel Sent";
  public static final String RM_CREATE_REQUEST_RECEIVED = "RM_CREATE_REQUEST_RECEIVED";
  public static final String RM_UPDATE_REQUEST_RECEIVED = "RM_UPDATE_REQUEST_RECEIVED";
  public static final String RM_CANCEL_REQUEST_RECEIVED = "RM_CANCEL_REQUEST_RECEIVED";

  public static final String INVALID_ACTION_INSTRUCTION = "INVALID_ACTION_INSTRUCTION";
  public static final String FAILED_TO_UNMARSHALL_ACTION_INSTRUCTION = "FAILED_TO_UNMARSHALL_ACTION_INSTRUCTION";
  public static final String FAILED_TO_MARSHALL_CANONICAL = "FAILED_TO_MARSHALL_CANONICAL";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.setSource(Application.APPLICATION_NAME);
    gatewayEventManager.addEventTypes(new String[] {CANONICAL_CREATE_SENT, CANONICAL_UPDATE_SENT, CANONICAL_CANCEL_SENT, RM_CREATE_REQUEST_RECEIVED,
        RM_UPDATE_REQUEST_RECEIVED, RM_CANCEL_REQUEST_RECEIVED});
    gatewayEventManager.addErrorEventTypes(new String[] {INVALID_ACTION_INSTRUCTION, FAILED_TO_UNMARSHALL_ACTION_INSTRUCTION, FAILED_TO_MARSHALL_CANONICAL});
    return gatewayEventManager;
  }
}
