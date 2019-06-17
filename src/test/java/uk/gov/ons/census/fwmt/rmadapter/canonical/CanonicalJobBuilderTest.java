package uk.gov.ons.census.fwmt.rmadapter.canonical;

import org.junit.Test;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import java.time.OffsetDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CanonicalJobBuilderTest {

  @Test
  public void createJobHH() throws GatewayException {
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
  public void createJobCEE() throws GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilderCEE();

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
    assertEquals("CE", result.getCaseType());
  }

  @Test
  public void createJobCEU() throws GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilderCEU();

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
    assertEquals("CE Unit Level", result.getCaseType());
  }

  @Test
  public void cancelJob() {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.cancelActionInstructionBuilder();

    //When
    CancelFieldWorkerJobRequest result = CanonicalJobHelper.newCancelJob(actionInstruction);

    //Then
    assertEquals("HQ Case Closure", result.getReason());
  }

  @Test
  public void updateJob() throws DatatypeConfigurationException{
    //Given
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilder();

    //When
    UpdateFieldWorkerJobRequest result = CanonicalJobHelper.newUpdateJob(actionInstruction);

    //Then
    assertEquals(actionInstruction.getActionUpdate().getCaseId(), String.valueOf(result.getCaseId()));
    assertEquals(actionInstruction.getActionUpdate().getAddressType(), result.getAddressType());
    assertEquals(actionInstruction.getActionUpdate().getActionableFrom().toString(), result.getHoldUntil().toString());

  }

  @Test
  public void createNisraJob() throws DatatypeConfigurationException, GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createNisraActionInstructionBuilder();

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
    assertEquals(actionInstruction.getActionRequest().getFieldOfficerId(), result.getMandatoryResource());
  }

  @Test
  public void cancelJobWithPause() {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.cancelActionInstructionBuilderForPause();

    //When
    CancelFieldWorkerJobRequest result = CanonicalJobHelper.newCancelJob(actionInstruction);

    //Then
    assertEquals(actionInstruction.getActionCancel().getReason(), result.getReason());
    assertEquals(actionInstruction.getActionCancel().getAddressType(), "HH");
    assertNotNull(result.getUntil());
  }

  @Test
  public void cancelJobWithPauseNonHH() {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.cancelActionInstructionBuilderForNonHouseHold();

    //When
    CancelFieldWorkerJobRequest result = CanonicalJobHelper.newCancelJob(actionInstruction);

    //Then
    assertNull(result.getReason());
    assertNull(result.getUntil());
  }
}