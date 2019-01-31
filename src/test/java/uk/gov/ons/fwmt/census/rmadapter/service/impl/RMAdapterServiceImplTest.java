package uk.gov.ons.fwmt.census.rmadapter.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.rmadapter.canonical.CanonicalJobBuilder;
import uk.gov.ons.fwmt.census.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.fwmt.census.rmadapter.message.JobServiceProducer;

@RunWith(MockitoJUnitRunner.class)
public class RMAdapterServiceImplTest {

  @InjectMocks
  private RMAdapterServiceImpl rmAdapterService;

  @Mock
  private JobServiceProducer jobServiceProducer;

  @Mock
  private CanonicalJobBuilder messageConverter;

  @Captor
  private ArgumentCaptor argCaptor;

  @Test
  public void sendCreateJobRequest() throws GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilder();
    CreateFieldWorkerJobRequest createJobRequest = new CreateFieldWorkerJobRequest();
    when(messageConverter.newCreateJob(actionInstruction)).thenReturn(createJobRequest);

    //When
    rmAdapterService.sendJobRequest(actionInstruction);

    //Then
    verify(jobServiceProducer).sendMessage(createJobRequest);
  }

  //  @Ignore("Code to update needs to be written")
  @Test
  public void sendUpdateJobRequest() throws GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.updateActionInstructionBuilder();
    UpdateFieldWorkerJobRequest fwmtUpdateJobRequest = new UpdateFieldWorkerJobRequest();
    when(messageConverter.newUpdateJob(actionInstruction)).thenReturn(fwmtUpdateJobRequest);

    //When
    rmAdapterService.sendJobRequest(actionInstruction);

    //Then
    verify(jobServiceProducer).sendMessage(fwmtUpdateJobRequest);
  }

  @Test
  public void sendCancelJobRequest() throws GatewayException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.cancelActionInstructionBuilder();
    CancelFieldWorkerJobRequest fwmtCancelJobRequest = new CancelFieldWorkerJobRequest();
    when(messageConverter.newCancelJob(actionInstruction)).thenReturn(fwmtCancelJobRequest);

    //When
    rmAdapterService.sendJobRequest(actionInstruction);

    //Then
    verify(jobServiceProducer).sendMessage(fwmtCancelJobRequest);
  }


}