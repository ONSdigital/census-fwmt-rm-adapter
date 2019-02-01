package uk.gov.ons.fwmt.census.rmadapter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.rmadapter.canonical.CanonicalJobBuilder;
import uk.gov.ons.fwmt.census.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.fwmt.census.rmadapter.service.RMAdapterService;

@Slf4j
@Component
public class RMAdapterServiceImpl implements RMAdapterService {

  @Autowired
  private GatewayActionProducer jobServiceProducer;

  public void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionRequest() != null) {
      jobServiceProducer.sendMessage(CanonicalJobBuilder.newCreateJob(actionInstruction));
    } else if (actionInstruction.getActionUpdate() != null) {
      jobServiceProducer.sendMessage(CanonicalJobBuilder.newUpdateJob(actionInstruction));
    } else if (actionInstruction.getActionCancel() != null) {
      jobServiceProducer.sendMessage(CanonicalJobBuilder.newCancelJob(actionInstruction));
    }
  }
}
