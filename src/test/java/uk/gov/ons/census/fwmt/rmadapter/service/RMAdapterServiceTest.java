package uk.gov.ons.census.fwmt.rmadapter.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.canonical.CanonicalJobHelper;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.census.fwmt.rmadapter.message.ActionInstructionReceiver;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdRequestEntity;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdStore;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.JobCacheManagerImpl;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.RMAdapterServiceImpl;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionUpdate;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.*;

@RunWith(MockitoJUnitRunner.class)
public class RMAdapterServiceTest {

  @InjectMocks
  private RMAdapterServiceImpl rmAdapterService;

  @Mock
  private HouseholdStore householdStore;

  @Mock
  private GatewayEventManager gatewayEventManager;

  @Mock
  private GatewayActionProducer gatewayActionProducer;

  @Mock
  private JobCacheManagerImpl jobCacheManager;

  private String caseId = "8ed3fc08-e95f-44db-a6d7-cde4e76a6182";

  @Test
  public void sendCreateMessage() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().createActionInstructionBuilder();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
        .forClass(ActionInstruction.class);

    rmAdapterService.sendJobRequest(actionInstruction);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_CREATE_SENT));
  }

  @Test
  public void sendCancelMessage() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilder();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_CANCEL_SENT));
  }

  @Test(expected = GatewayException.class)
  public void sendCancelWithoutCreate() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilder();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    when(householdStore.retrieveCache(caseId)).thenReturn(null);

    rmAdapterService.sendJobRequest(actionInstruction);

  }

  @Test(expected = GatewayException.class)
  public void sendCancelMessageWithOutHH() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilderForNonHouseHold();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);
  }

  @Test
  public void sendUpdateMessage() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilder();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_UPDATE_SENT));
  }

  @Test(expected = GatewayException.class)
  public void sendUpdateWithoutCreate() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilder();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    when(householdStore.retrieveCache(caseId)).thenReturn(null);

    rmAdapterService.sendJobRequest(actionInstruction);

  }

  @Test(expected = GatewayException.class)
  public void sendUpdateMessageWithoutHH() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilderForNonHousehold();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);
  }

  @Test(expected = GatewayException.class)
  public void sendIncorrectMessageActionInstruction() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstruction();

    ArgumentCaptor<ActionInstruction> actionInstructionArgumentCaptor = ArgumentCaptor
            .forClass(ActionInstruction.class);

    rmAdapterService.sendJobRequest(actionInstruction);
  }
}
