package uk.gov.ons.fwmt.census.rmadapter.service;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.common.error.GatewayException;

public interface RMAdapterService {

  void sendJobRequest(ActionInstruction actionInstruction) throws GatewayException;

}
