package uk.gov.ons.census.fwmt.rmadapter.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayActionsQueueConfig {

  public static final String GATEWAY_ACTIONS_QUEUE = "Gateway.Actions";
  public static final String GATEWAY_ACTIONS_EXCHANGE = "Gateway.Actions.Exchange";
  public static final String GATEWAY_ACTIONS_ROUTING_KEY = "Gateway.Action.Request";

}
