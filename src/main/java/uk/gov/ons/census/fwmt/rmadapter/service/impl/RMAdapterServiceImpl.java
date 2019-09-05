package uk.gov.ons.census.fwmt.rmadapter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdStore;
import uk.gov.ons.census.fwmt.rmadapter.service.RMAdapterService;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.*;

@Slf4j
@Component
public class RMAdapterServiceImpl implements RMAdapterService {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayActionProducer jobServiceProducer;

  @Autowired
  private HouseholdStore householdStore;

  public void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionRequest() != null) {
      sendCreateMessage(actionInstruction);
    } else if (actionInstruction.getActionCancel() != null) {
      createCancelMessage(actionInstruction);
    } else if (actionInstruction.getActionUpdate() != null) {
      createUpdateMessage(actionInstruction);
    } else {
      String msg = "No matching request was found";
      String unknown = "Unknown caseId";
      gatewayEventManager.triggerErrorEvent(this.getClass(), msg, unknown, INVALID_ACTION_INSTRUCTION);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, msg);
    }
  }

  private void createUpdateMessage(ActionInstruction actionInstruction) throws GatewayException {
    if (householdStore.retrieveCache(actionInstruction.getActionUpdate().getCaseId()) == null)
      return;

    if (actionInstruction.getActionUpdate().getAddressType().equals("HH")) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newUpdateJob(actionInstruction));
      gatewayEventManager.triggerEvent(actionInstruction.getActionUpdate().getCaseId(), CANONICAL_UPDATE_SENT);
    } else {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Valid address type not found");
    }
  }

  private void createCancelMessage(ActionInstruction actionInstruction) throws GatewayException {
    if (householdStore.retrieveCache(actionInstruction.getActionCancel().getCaseId()) == null)
      return;

    if (householdStore.retrieveCache(actionInstruction.getActionCancel().getCaseId()) != null) {
      if (actionInstruction.getActionCancel().getAddressType().equals("HH")) {

        jobServiceProducer.sendMessage(CanonicalJobHelper.newCancelJob(actionInstruction));
        gatewayEventManager.triggerEvent(actionInstruction.getActionCancel().getCaseId(), CANONICAL_CANCEL_SENT);
      } else {
        throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Valid address type not found");
      }
    }
  }

  private void sendCreateMessage(ActionInstruction actionInstruction) throws GatewayException {
    householdStore.cacheJob(actionInstruction.getActionRequest().getCaseId());
    jobServiceProducer.sendMessage(CanonicalJobHelper.newCreateJob(actionInstruction));
    gatewayEventManager.triggerEvent(actionInstruction.getActionRequest().getCaseId(), CANONICAL_CREATE_SENT);
  }
}
