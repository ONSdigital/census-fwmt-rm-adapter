package uk.gov.ons.census.fwmt.rmadapter.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.census.fwmt.rmadapter.service.RMAdapterService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.time.LocalTime;

import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CREATE_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_UPDATE_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.RM_REQUEST_RECEIVED;

@Component
@Slf4j
public class ActionInstructionReceiver {

  @Autowired
  private RMAdapterService rmAdapterService;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  private JAXBContext jaxbContext;

  public ActionInstructionReceiver() throws JAXBException {
    jaxbContext = JAXBContext.newInstance(ActionInstruction.class);
  }

  public void receiveMessage(String message) throws GatewayException {
    try {
      // This should be moved to Queue Config, but cant get it to work
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      ByteArrayInputStream input = new ByteArrayInputStream(message.getBytes());
      JAXBElement<ActionInstruction> rmActionInstruction = unmarshaller
          .unmarshal(new StreamSource(input), ActionInstruction.class);
      // ===============================================================
      triggerEvent(rmActionInstruction.getValue());
      rmAdapterService.sendJobRequest(rmActionInstruction.getValue());
      log.info("Received Job request from RM");
    } catch (JAXBException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to unmarshal XML message.", e);
    }
  }

  private void triggerEvent(ActionInstruction actionInstruction) throws GatewayException {
    if (actionInstruction.getActionRequest() != null) {
      gatewayEventManager.triggerEvent(actionInstruction.getActionRequest().getCaseId(), RM_REQUEST_RECEIVED, LocalTime.now());
    } else if (actionInstruction.getActionCancel() != null) {
      gatewayEventManager.triggerEvent(actionInstruction.getActionCancel().getCaseId(), RM_REQUEST_RECEIVED, LocalTime.now());
    }
  }
}
