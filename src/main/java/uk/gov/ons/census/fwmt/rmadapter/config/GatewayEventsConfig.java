package uk.gov.ons.census.fwmt.rmadapter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;

@Configuration
public class GatewayEventsConfig {

  public static final String CANONICAL_CREATE_SENT = "Canonical - Action Create Sent";
  public static final String CANONICAL_UPDATE_SENT = "Canonical - Action Update Sent";
  public static final String CANONICAL_CANCEL_SENT = "Canonical - Action Cancel Sent";
  public static final String RM_REQUEST_RECEIVED = "RM - Request Received";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.addEventTypes(new String[] {CANONICAL_CREATE_SENT, CANONICAL_UPDATE_SENT, CANONICAL_CANCEL_SENT,
        RM_REQUEST_RECEIVED});
    return gatewayEventManager;
  }
}
