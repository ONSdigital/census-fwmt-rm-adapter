package uk.gov.ons.fwmt.census.rmadapter.message;

import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface JobServiceProducer {
  void sendMessage(Object klass) throws CTPException;
}
