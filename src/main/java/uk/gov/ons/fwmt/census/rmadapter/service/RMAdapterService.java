package uk.gov.ons.fwmt.census.rmadapter.service;

import javax.xml.bind.JAXBException;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface RMAdapterService {

  void sendJobRequest(ActionInstruction actionInstruction) throws JAXBException, CTPException;

}
