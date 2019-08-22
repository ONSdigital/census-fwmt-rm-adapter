package uk.gov.ons.census.fwmt.rmadapter.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.service.JobCacheManager;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.JobCacheManagerImpl;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.RMAdapterServiceImpl;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

import javax.xml.datatype.DatatypeConfigurationException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.ons.census.fwmt.rmadapter.config.GatewayEventsConfig.*;

@RunWith(MockitoJUnitRunner.class)
public class HouseholdStoreTest {

  @InjectMocks
  private HouseholdStore householdStore;

  @Mock
  private JobCacheManager jobCacheManager;

  @Mock
  private JobCacheManagerImpl jobCacheManagerImpl;

  private String caseId = "8ed3fc08-e95f-44db-a6d7-cde4e76a6182";

  @Test
  public void sendCreateMessage() throws GatewayException {
    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    householdStore.cacheJob(caseId);
    verify(jobCacheManager).cacheCreateHouseholdRequest(householdRequestEntity);
  }

}
