package uk.gov.ons.fwmt.census.rmadapter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.fwmt.census.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.fwmt.census.rmadapter.message.GatewayEventProducer;
import uk.gov.ons.fwmt.census.rmadapter.service.RMAdapterService;

@Slf4j
@Component
public class RMAdapterServiceImpl implements RMAdapterService {

  @Autowired
  private GatewayEventProducer gatewayEventProducer;

  @Autowired
  private GatewayActionProducer jobServiceProducer;

  public void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionRequest() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newCreateJob(actionInstruction));
      gatewayEventProducer
          .sendEvent(actionInstruction.getActionRequest().getCaseId(), "Canonical - Action Create Sent");
    } else if (actionInstruction.getActionUpdate() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newUpdateJob(actionInstruction));
      gatewayEventProducer
          .sendEvent(actionInstruction.getActionRequest().getCaseId(), "Canonical - Action Update Sent");
    } else if (actionInstruction.getActionCancel() != null) {
      jobServiceProducer.sendMessage(CanonicalJobHelper.newCancelJob(actionInstruction));
      gatewayEventProducer
          .sendEvent(actionInstruction.getActionRequest().getCaseId(), "Canonical - Action Cancel Sent");
    }
  }
}
