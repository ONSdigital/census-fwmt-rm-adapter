package uk.gov.ons.census.fwmt.rmadapter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.CANONICAL_CREATE_SENT;

@RunWith(MockitoJUnitRunner.class)
public class RMAdapterServiceImplTest {

  @InjectMocks
  private uk.gov.ons.census.fwmt.rmadapter.service.impl.RMAdapterServiceImpl rmAdapterServiceImpl;

  @Mock
  private GatewayEventManager gatewayEventManager;

  @Mock
  private GatewayActionProducer gatewayActionProducer;

  @Test
  public void sendJobActionRequest() throws GatewayException {
    // Given
    ActionInstruction actionInstruction = new ActionInstructionBuilder().createActionInstructionBuilder();
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = CanonicalJobHelper.newCreateJob(actionInstruction);

    // When
    rmAdapterServiceImpl.sendJobRequest(actionInstruction);

    // Then
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_CREATE_SENT), any());
  }

  @Test
  public void sendJobActionCancel() throws GatewayException {
    // Given
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilder();
    CancelFieldWorkerJobRequest createFieldWorkerJobRequest = CanonicalJobHelper.newCancelJob(actionInstruction);

    // When
    rmAdapterServiceImpl.sendJobRequest(actionInstruction);

    // Then
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_CANCEL_SENT), any());
  }
}
