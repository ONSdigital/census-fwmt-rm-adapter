package uk.gov.ons.census.fwmt.rmadapter.message;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.RMAdapterServiceImpl;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionPause;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ActionInstructionReceiverTest {

  @InjectMocks
  private ActionInstructionReceiver actionInstructionReceiver;

  @Mock
  private RMAdapterServiceImpl rmAdapterService;

  @Mock
  private GatewayEventManager gatewayEventManager;
  
  private String ACTION_CANCEL_XML;

  private String ACTION_REQUEST_XML;

  private String ACTION_UPDATE_PAUSE_XML;

  private String ACTION_UPDATE_PAUSE_NO_CASEID_XML;
  
  @Before
  public void setup() throws IOException {
    ACTION_CANCEL_XML = Resources.toString(Resources.getResource("ActionInstructionReceiverTest/ACTION_CANCEL_XML.xml"), Charsets.UTF_8);
    ACTION_REQUEST_XML = Resources.toString(Resources.getResource("ActionInstructionReceiverTest/ACTION_REQUEST_XML.xml"), Charsets.UTF_8);
    ACTION_UPDATE_PAUSE_XML = Resources.toString(Resources.getResource("ActionInstructionReceiverTest/ACTION_UPDATE_PAUSE_XML.xml"), Charsets.UTF_8);
    ACTION_UPDATE_PAUSE_NO_CASEID_XML = Resources.toString(Resources.getResource("ActionInstructionReceiverTest/ACTION_UPDATE_PAUSE_NO_CASEID_XML.xml"), Charsets.UTF_8);
  }

  @Test
  public void receiveMessageCreate() throws GatewayException {

    actionInstructionReceiver.receiveMessage(ACTION_REQUEST_XML);

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
        .forClass(ActionInstruction.class);

    verify(rmAdapterService).sendJobRequest(actionInstructionArgumentCaptor.capture());

    ActionInstruction actionInstruction = actionInstructionArgumentCaptor.getValue();

    assertNotNull(actionInstruction.getActionRequest());
    assertNull(actionInstruction.getActionCancel());
    assertNull(actionInstruction.getActionUpdate());

    ActionRequest actionRequest = actionInstruction.getActionRequest();

    assertEquals(actionRequest.getCaseId(), "caseId");
    assertEquals(actionRequest.getActionType(), "actionType");
    assertEquals(actionRequest.getActionId(), "actionId");
    assertEquals(actionRequest.getReturnByDate(), "19950718");
    assertEquals(actionRequest.getSurveyRef(), "surveyRef");

    ActionAddress address = actionRequest.getAddress();

    assertEquals(address.getLatitude(), BigDecimal.valueOf(1234.56));
    assertEquals(address.getLongitude(), BigDecimal.valueOf(2345.67));
    assertEquals(address.getLine1(), "line1");
    assertEquals(address.getLine2(), "line2");
    assertEquals(address.getPostcode(), "P05T C0D3");
    assertEquals(address.getTownName(), "Town");

    verify(rmAdapterService).sendJobRequest(actionInstruction);
  }

  @Test
  public void receiveMessageCancel() throws GatewayException {

    actionInstructionReceiver.receiveMessage(ACTION_CANCEL_XML);

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
        .forClass(ActionInstruction.class);

    verify(rmAdapterService).sendJobRequest(actionInstructionArgumentCaptor.capture());

    ActionInstruction actionInstruction = actionInstructionArgumentCaptor.getValue();

    assertNotNull(actionInstruction.getActionCancel());
    assertNull(actionInstruction.getActionRequest());
    assertNull(actionInstruction.getActionUpdate());

    ActionCancel actionCancel = actionInstruction.getActionCancel();

    assertEquals(actionCancel.getReason(), "Reason");
    assertEquals(actionCancel.getActionId(), "actionId");

    verify(rmAdapterService).sendJobRequest(actionInstruction);
  }

  @Test
  public void receiveMessageUpdatePause() throws GatewayException {
    actionInstructionReceiver.receiveMessage(ACTION_UPDATE_PAUSE_XML);

    ArgumentCaptor <ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor.forClass(ActionInstruction.class);

    verify(rmAdapterService).sendJobRequest(actionInstructionArgumentCaptor.capture());

    ActionInstruction actionInstruction = actionInstructionArgumentCaptor.getValue();

    assertNotNull(actionInstruction.getActionUpdate());

    ActionPause actionPause = actionInstruction.getActionUpdate().getPause();

    assertEquals(String.valueOf("8ed3fc08-e95f-44db-a6d7-cde4e76a6182"), actionPause.getId());
    assertEquals("2019-05-27", actionPause.getUntil().toString());
    verify(rmAdapterService).sendJobRequest(actionInstruction);
  }

  @Test
  public void receiveMessageUpdatePauseNoCaseId() throws GatewayException {
    actionInstructionReceiver.receiveMessage(ACTION_UPDATE_PAUSE_NO_CASEID_XML);

    ArgumentCaptor <ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor.forClass(ActionInstruction.class);

    verify(rmAdapterService).sendJobRequest(actionInstructionArgumentCaptor.capture());

    ActionInstruction actionInstruction = actionInstructionArgumentCaptor.getValue();

    assertNotNull(actionInstruction.getActionUpdate());

    ActionPause actionPause = actionInstruction.getActionUpdate().getPause();

    assertEquals(String.valueOf(""), actionPause.getId());
    verify(rmAdapterService).sendJobRequest(actionInstruction);
  }

}
