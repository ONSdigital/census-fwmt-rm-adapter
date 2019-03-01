package uk.gov.ons.census.fwmt.rmadapter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.service.RMAdapterService;

import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CREATE_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_UPDATE_SENT;

@Slf4j
@Component
public class RMAdapterServiceImpl implements RMAdapterService {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayActionProducer jobServiceProducer;

  public void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionRequest() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newCreateJob(actionInstruction));
      gatewayEventManager.triggerEvent(actionInstruction.getActionRequest().getCaseId(), CANONICAL_CREATE_SENT);
    } else if (actionInstruction.getActionCancel() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newCancelJob(actionInstruction));
      gatewayEventManager.triggerEvent(actionInstruction.getActionCancel().getCaseId(), CANONICAL_CANCEL_SENT);
    }
  }
}
