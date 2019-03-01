package uk.gov.ons.census.fwmt.rmadapter.service;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface RMAdapterService {

  void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException;

}
