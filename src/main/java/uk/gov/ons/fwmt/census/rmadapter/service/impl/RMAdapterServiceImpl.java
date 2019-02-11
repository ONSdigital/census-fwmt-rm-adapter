package uk.gov.ons.fwmt.census.rmadapter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.events.component.GatewayEventManager;
import uk.gov.ons.fwmt.census.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.fwmt.census.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.fwmt.census.rmadapter.service.RMAdapterService;

import static uk.gov.ons.fwmt.census.rmadapter.config.GatewayEventsConfig.CANONICAL_CANCEL_SENT;
import static uk.gov.ons.fwmt.census.rmadapter.config.GatewayEventsConfig.CANONICAL_CREATE_SENT;
import static uk.gov.ons.fwmt.census.rmadapter.config.GatewayEventsConfig.CANONICAL_UPDATE_SENT;

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
      gatewayEventManager
          .triggerEvent(actionInstruction.getActionRequest().getCaseId(), CANONICAL_CREATE_SENT);
    } else if (actionInstruction.getActionUpdate() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newUpdateJob(actionInstruction));
      gatewayEventManager
          .triggerEvent(actionInstruction.getActionRequest().getCaseId(), CANONICAL_UPDATE_SENT);
    } else if (actionInstruction.getActionCancel() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newCancelJob(actionInstruction));
      gatewayEventManager
          .triggerEvent(actionInstruction.getActionRequest().getCaseId(), CANONICAL_CANCEL_SENT);
    }
  }
}
