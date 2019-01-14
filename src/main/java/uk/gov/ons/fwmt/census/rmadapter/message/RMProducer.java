package uk.gov.ons.fwmt.census.rmadapter.message;

import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface RMProducer {

  void sendJobRequestResponse(CensusCaseOutcomeDTO createJobRequest) throws CTPException;
}
