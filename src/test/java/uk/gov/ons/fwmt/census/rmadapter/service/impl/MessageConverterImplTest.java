package uk.gov.ons.fwmt.census.rmadapter.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.rmadapter.canonical.CanonicalJobBuilder;
import uk.gov.ons.fwmt.census.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTUpdateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class MessageConverterImplTest {

  @InjectMocks CanonicalJobBuilder messageConverter;

  @Test
  public void createJob() throws CTPException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilder();

    //When
    FWMTCreateJobRequest result = messageConverter.newCreateJob(actionInstruction);

    //Then
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    assertEquals(actionInstruction.getActionRequest().getSurveyRef(), result.getSurveyType());
    assertEquals(
        LocalDate.parse(actionInstruction.getActionRequest().getReturnByDate(), formatter),
        result.getDueDate());
    assertEquals(actionInstruction.getActionRequest().getAddress().getLatitude(),
        result.getAddress().getLatitude());
    assertEquals(actionInstruction.getActionRequest().getAddress().getLongitude(),
        result.getAddress().getLongitude());
    assertEquals(actionInstruction.getActionRequest().getAddress().getPostcode(),
        result.getAddress().getPostCode());
    assertEquals(actionInstruction.getActionRequest().getAddress().getTownName(),
        result.getAddress().getTownName());
    assertEquals(actionInstruction.getActionRequest().getAddress().getLine1(), result.getAddress().getLine1());
    assertEquals(actionInstruction.getActionRequest().getAddress().getLine2(), result.getAddress().getLine2());
  }

  @Test
  public void cancelJob() {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.cancelActionInstructionBuilder();

    //When
    FWMTCancelJobRequest result = messageConverter.newCancelJob(actionInstruction);

    //Then
    assertEquals("testCaseRef", result.getJobIdentity());
    assertEquals(actionInstruction.getActionCancel().getReason(), result.getReason());
  }

  @Test
  public void updateJob() {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.updateActionInstructionBuilder();

    //When
    FWMTUpdateJobRequest result = messageConverter.updateJob(actionInstruction);

    //Then
    assertNotNull(result);
  }
}