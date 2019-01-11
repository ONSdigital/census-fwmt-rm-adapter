package uk.gov.ons.fwmt.census.rmadapter.message.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.rmadapter.message.JobSvcReceiver;
import uk.gov.ons.fwmt.census.rmadapter.service.RMAdapterService;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

import java.io.IOException;

@Slf4j
@Component
public class JobServiceReceiverImpl implements JobSvcReceiver {

  @Autowired
  private RMAdapterService rmAdapterService;

  @Autowired
  private ObjectMapper objectMapper;

  public void receiveMessage(String returnJobRequestXML) throws CTPException {
    try {
      final CensusCaseOutcomeDTO response = objectMapper.readValue(returnJobRequestXML, CensusCaseOutcomeDTO.class);
      rmAdapterService.returnJobRequest(response);
    } catch (IOException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Failed to map response.", e);
    }
  }
}
