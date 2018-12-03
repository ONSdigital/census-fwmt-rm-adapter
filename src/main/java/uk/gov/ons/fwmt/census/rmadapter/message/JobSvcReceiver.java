package uk.gov.ons.fwmt.census.rmadapter.message;

import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface JobSvcReceiver {

  void receiveMessage(String returnJobRequestXML) throws CTPException;
}
