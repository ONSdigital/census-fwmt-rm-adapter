package uk.gov.ons.census.fwmt.rmadapter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.rmadapter.config.RedisUtil;
import uk.gov.ons.census.fwmt.rmadapter.redis.HouseholdRequestEntity;
import uk.gov.ons.census.fwmt.rmadapter.service.JobCacheManager;

@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private RedisUtil<HouseholdRequestEntity> redisUtil;

  @Override
  public HouseholdRequestEntity cacheCreateHouseholdRequest(HouseholdRequestEntity householdRequestEntity) {
    redisUtil.putValue(householdRequestEntity.getCaseId(), householdRequestEntity);
    log.info("Placed the following in cache: " +  householdRequestEntity.toString());
    return householdRequestEntity;
  }

  @Override
  public HouseholdRequestEntity getCachedHouseholdCaseId(String caseId) {
    HouseholdRequestEntity householdRequestEntity = redisUtil.getValue(caseId);
    if (householdRequestEntity != null) {
      log.info("Received object from cache: " + householdRequestEntity.toString());
    }
    return householdRequestEntity;
  }
}

