package uk.gov.ons.census.fwmt.rmadapter.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.rmadapter.config.RedisUtil;
import uk.gov.ons.census.fwmt.rmadapter.helper.ActionInstructionBuilder;
import uk.gov.ons.census.fwmt.rmadapter.message.GatewayActionProducer;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdRequestEntity;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdStore;
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
public class JobCacheManagerTest {

  @InjectMocks
  private JobCacheManagerImpl jobCacheManagerImpl;

  @Mock
  private RedisUtil<HouseholdRequestEntity> redisUtil;

  private String caseId = "8ed3fc08-e95f-44db-a6d7-cde4e76a6182";

  @Test
  public void cacheCreateHouseholdRequestTest() throws GatewayException {
    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    jobCacheManagerImpl.cacheCreateHouseholdRequest(householdRequestEntity);
    verify(redisUtil).putValue(caseId, householdRequestEntity);
  }

  @Test
  public void getCachedHouseholdCaseIdTest() throws GatewayException {
    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();
    householdRequestEntity.setCaseId(caseId);

    when(jobCacheManagerImpl.getCachedHouseholdCaseId(caseId)).thenReturn(householdRequestEntity);

    jobCacheManagerImpl.getCachedHouseholdCaseId(caseId);

    verify(redisUtil).getValue(caseId);
  }
}
