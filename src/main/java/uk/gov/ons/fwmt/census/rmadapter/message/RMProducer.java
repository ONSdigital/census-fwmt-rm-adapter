package uk.gov.ons.fwmt.census.rmadapter.message;

import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;

public interface RMProducer {

  void sendJobRequestResponse(FwmtOHSJobStatusNotification createJobRequest) throws CTPException;
}
