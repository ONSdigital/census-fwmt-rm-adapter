package uk.gov.ons.fwmt.census.rmadapter.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GatewayEventDTO implements Serializable {

  private String caseId;
  private String eventType;
}
