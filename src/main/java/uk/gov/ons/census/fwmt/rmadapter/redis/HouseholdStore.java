package uk.gov.ons.census.fwmt.rmadapter.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.rmadapter.service.JobCacheManager;


@Component
public class HouseholdStore {

  @Autowired
  private JobCacheManager jobCacheManager;

  public HouseholdRequestEntity cacheJob(String caseId) {
    HouseholdRequestEntity householdRequestEntity = new HouseholdRequestEntity();

    householdRequestEntity.setCaseId(caseId);
    householdRequestEntity.setHasBeenRecorded("true");

    return jobCacheManager.cacheCreateHouseholdRequest(householdRequestEntity);
  }

  public HouseholdRequestEntity retrieveCache(String caseId) {
    return jobCacheManager.getCachedHouseholdCaseId(caseId);
  }
}
