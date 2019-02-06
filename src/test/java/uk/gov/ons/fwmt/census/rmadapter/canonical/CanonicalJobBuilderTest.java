package uk.gov.ons.fwmt.census.rmadapter.canonical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.rmadapter.helper.ActionInstructionBuilder;

@RunWith(MockitoJUnitRunner.class)
public class CanonicalJobBuilderTest {

  @InjectMocks CanonicalJobHelper messageConverter;

  @Test
  public void createJob() throws GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilder();

    //When
    CreateFieldWorkerJobRequest result = messageConverter.newCreateJob(actionInstruction);

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
    CancelFieldWorkerJobRequest result = messageConverter.newCancelJob(actionInstruction);

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
    UpdateFieldWorkerJobRequest result = messageConverter.newUpdateJob(actionInstruction);

    //Then
    assertNotNull(result);
  }
}