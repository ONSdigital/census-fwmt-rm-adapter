package uk.gov.ons.census.fwmt.rmadapter.service;


import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdRequestEntity;

public interface JobCacheManager {

  HouseholdRequestEntity cacheCreateHouseholdRequest(HouseholdRequestEntity householdRequestEntity);

  HouseholdRequestEntity getCachedHouseholdCaseId(String caseId);

}
