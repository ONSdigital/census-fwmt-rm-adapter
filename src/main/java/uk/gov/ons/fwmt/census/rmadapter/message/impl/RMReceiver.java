package uk.gov.ons.fwmt.census.rmadapter.message.impl;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.rmadapter.service.RMAdapterService;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

@Component
@Slf4j
public class RMReceiver {

  @Autowired
  private RMAdapterService rmAdapterService;

  private JAXBContext jaxbContext;

  public RMReceiver() throws JAXBException {
    jaxbContext = JAXBContext.newInstance(ActionInstruction.class);
  }

  public void receiveMessage(String createJobRequestXML) throws CTPException {
    try {
      //TODO Move this Queue Config
      //===================================================
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      ByteArrayInputStream input = new ByteArrayInputStream(createJobRequestXML.getBytes());
      JAXBElement<ActionInstruction> rmActionInstruction = unmarshaller
          .unmarshal(new StreamSource(input), ActionInstruction.class);
      //===================================================
      rmAdapterService.sendJobRequest(rmActionInstruction.getValue());
      log.info("Received Job request from RM");
    } catch (JAXBException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Failed to unmarshal XML message.", e);
    }
  }
}
