package uk.gov.ons.fwmt.census.rmadapter.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.rmadapter.service.RMAdapterService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

@Component
@Slf4j
public class ActionInstructionReceiver {

  @Autowired
  private RMAdapterService rmAdapterService;

  @Autowired
  private GatewayEventProducer gatewayEventProducer;

  public void receiveMessage(String message) throws GatewayException {
    try {
      // This should be moved to Queue Config, but cant get it to work
      //==============================================================
      JAXBContext jaxbContext = JAXBContext.newInstance(ActionInstruction.class);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      ByteArrayInputStream input = new ByteArrayInputStream(message.getBytes());
      JAXBElement<ActionInstruction> rmActionInstruction = unmarshaller
          .unmarshal(new StreamSource(input), ActionInstruction.class);
      //===============================================================
      gatewayEventProducer
          .sendEvent(rmActionInstruction.getValue().getActionRequest().getCaseId(), "- RM - Request Received");
      rmAdapterService.sendJobRequest(rmActionInstruction.getValue());
      log.info("Received Job request from RM");
    } catch (JAXBException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to unmarshal XML message.", e);
    }
  }
}
