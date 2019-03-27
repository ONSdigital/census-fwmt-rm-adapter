package uk.gov.ons.census.fwmt.rmadapter.canonical;

import org.junit.Test;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CanonicalJobBuilderTest {

  @Test
  public void createJob() throws GatewayException, DatatypeConfigurationException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilder();

    //When
    CreateFieldWorkerJobRequest result = CanonicalJobHelper.newCreateJob(actionInstruction);

    //Then
    assertEquals(actionInstruction.getActionRequest().getCaseId(), String.valueOf(result.getCaseId()));
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
    CancelFieldWorkerJobRequest result = CanonicalJobHelper.newCancelJob(actionInstruction);

    //Then
    assertEquals(actionInstruction.getActionCancel().getReason(), result.getReason());
  }

  @Test
  public void updateJob() {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.updateActionInstructionBuilder();

    //When
    UpdateFieldWorkerJobRequest result = CanonicalJobHelper.newUpdateJob(actionInstruction);

    //Then
    assertNotNull(result);
  }
}