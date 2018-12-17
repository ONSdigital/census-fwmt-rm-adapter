package uk.gov.ons.fwmt.census.rmadapter.service;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;

import javax.xml.bind.JAXBException;

public interface RMAdapterService {

  void sendJobRequest(ActionInstruction actionInstruction) throws JAXBException, CTPException;

  void returnJobRequest(CensusCaseOutcomeDTO response) throws CTPException;

}
