package uk.gov.ons.census.fwmt.rmadapter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdRequestEntity;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdStore;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.RMAdapterServiceImpl;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

  private String caseId = "8ed3fc08-e95f-44db-a6d7-cde4e76a6182";

  @Test
  public void sendCreateMessage() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().createActionInstructionBuilder();

    rmAdapterService.sendJobRequest(actionInstruction);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_CREATE_SENT));
  }

  @Test
  public void sendCancelMessage() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilder();

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_CANCEL_SENT));
  }

  @Test(expected = GatewayException.class)
  public void sendCancelWithoutCreate() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilder();

    when(householdStore.retrieveCache(caseId)).thenReturn(null);

    rmAdapterService.sendJobRequest(actionInstruction);

  }

  @Test(expected = GatewayException.class)
  public void sendCancelMessageWithOutHH() throws GatewayException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().cancelActionInstructionBuilderForNonHouseHold();

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);
  }

  @Test
  public void sendUpdateMessage() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilder();

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);

    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(CANONICAL_UPDATE_SENT));
  }

  @Test(expected = GatewayException.class)
  public void sendUpdateWithoutCreate() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilder();

    when(householdStore.retrieveCache(caseId)).thenReturn(null);

    rmAdapterService.sendJobRequest(actionInstruction);

  }

  @Test(expected = GatewayException.class)
  public void sendUpdateMessageWithoutHH() throws GatewayException, DatatypeConfigurationException {
    ActionInstruction actionInstruction = new ActionInstructionBuilder().updateActionInstructionBuilderForNonHousehold();

    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(householdStore.retrieveCache(caseId)).thenReturn(householdRequestEntity);

    rmAdapterService.sendJobRequest(actionInstruction);
  }

  @Test(expected = GatewayException.class)
  public void sendIncorrectMessageActionInstruction() throws GatewayException{
    ActionInstruction actionInstruction = new ActionInstruction();

    rmAdapterService.sendJobRequest(actionInstruction);
  }
}
