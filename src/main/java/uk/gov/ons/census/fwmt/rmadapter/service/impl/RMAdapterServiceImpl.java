package uk.gov.ons.census.fwmt.rmadapter.service.impl;

import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CREATE_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_UPDATE_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.INVALID_ACTION_INSTRUCTION;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.service.RMAdapterService;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

@Slf4j
@Component
public class RMAdapterServiceImpl implements RMAdapterService {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayActionProducer jobServiceProducer;

  public void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionRequest() != null) {
      sendCreateMessage(actionInstruction);
    } else if (actionInstruction.getActionCancel() != null) {
      createCancelMessage(actionInstruction);
    } else if (actionInstruction.getActionUpdate() != null) {
      createUpdateMessage(actionInstruction);
    } else {
      String msg = "No matching request was found";
      gatewayEventManager.triggerErrorEvent(this.getClass(), msg, actionInstruction.getActionRequest().getCaseId(), INVALID_ACTION_INSTRUCTION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, msg);
    }
  }

  private void createUpdateMessage(ActionInstruction actionInstruction) throws GatewayException {
    jobServiceProducer.sendMessage(CanonicalJobHelper.newUpdateJob(actionInstruction));
    gatewayEventManager.triggerEvent(actionInstruction.getActionUpdate().getCaseId(), CANONICAL_UPDATE_SENT);
  }

  private void createCancelMessage(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionCancel().getAddressType().equals("HH")) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newCancelJob(actionInstruction));
      gatewayEventManager.triggerEvent(actionInstruction.getActionCancel().getCaseId(), CANONICAL_CANCEL_SENT);
    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Valid address type not found");
    }
  }

  private void sendCreateMessage(ActionInstruction actionInstruction) throws GatewayException {
    jobServiceProducer.sendMessage(CanonicalJobHelper.newCreateJob(actionInstruction));
    gatewayEventManager.triggerEvent(actionInstruction.getActionRequest().getCaseId(), CANONICAL_CREATE_SENT);
  }
}
