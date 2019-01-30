package uk.gov.ons.fwmt.census.rmadapter.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.fwmt.census.rmadapter.canonical.CanonicalJobBuilder;
import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.rmadapter.data.DummyRMReturn;
import uk.gov.ons.fwmt.census.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.fwmt.census.rmadapter.message.JobServiceProducer;
import uk.gov.ons.fwmt.census.rmadapter.message.RMProducer;
import uk.gov.ons.fwmt.census.rmadapter.service.impl.RMAdapterServiceImpl;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.DummyTMResponse;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTUpdateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RMAdapterServiceImplTest {

  @InjectMocks
  private RMAdapterServiceImpl rmAdapterService;

  @Mock
  private JobServiceProducer jobServiceProducer;

  @Mock
  private CanonicalJobBuilder messageConverter;

  @Mock
  private RMProducer rmProducer;

  @Captor
  private ArgumentCaptor argCaptor;

  @Test
  public void sendCreateJobRequest() throws CTPException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.createActionInstructionBuilder();
    FWMTCreateJobRequest fwmtCreateJobRequest = new FWMTCreateJobRequest();
    when(messageConverter.newCreateJob(actionInstruction)).thenReturn(fwmtCreateJobRequest);

    //When
    rmAdapterService.sendJobRequest(actionInstruction);

    //Then
    verify(jobServiceProducer).sendMessage(fwmtCreateJobRequest);
  }

  //  @Ignore("Code to update needs to be written")
  @Test
  public void sendUpdateJobRequest() throws CTPException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.updateActionInstructionBuilder();
    FWMTUpdateJobRequest fwmtUpdateJobRequest = new FWMTUpdateJobRequest();
    when(messageConverter.updateJob(actionInstruction)).thenReturn(fwmtUpdateJobRequest);

    //When
    rmAdapterService.sendJobRequest(actionInstruction);

    //Then
    verify(jobServiceProducer).sendMessage(fwmtUpdateJobRequest);
  }

  @Test
  public void sendCancelJobRequest() throws CTPException {
    //Given
    ActionInstructionBuilder actionInstructionBuilder = new ActionInstructionBuilder();
    ActionInstruction actionInstruction = actionInstructionBuilder.cancelActionInstructionBuilder();
    FWMTCancelJobRequest fwmtCancelJobRequest = new FWMTCancelJobRequest();
    when(messageConverter.newCancelJob(actionInstruction)).thenReturn(fwmtCancelJobRequest);

    //When
    rmAdapterService.sendJobRequest(actionInstruction);

    //Then
    verify(jobServiceProducer).sendMessage(fwmtCancelJobRequest);
  }

  @Test
  public void returnJobRequest() throws CTPException {
    //Given
    CensusCaseOutcomeDTO response = new CensusCaseOutcomeDTO();
    response.setCaseId("dummy");
//    response.setJobIdentity("dummy");

    //When
    rmAdapterService.returnJobRequest(response);

    //Then
    Mockito.verify(rmProducer).sendJobRequestResponse((CensusCaseOutcomeDTO) argCaptor.capture());
    CensusCaseOutcomeDTO result = (CensusCaseOutcomeDTO) argCaptor.getValue();
    assertEquals(response.getCaseId(), result.getCaseId());
  }


}