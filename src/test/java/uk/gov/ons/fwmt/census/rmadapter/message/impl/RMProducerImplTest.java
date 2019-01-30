package uk.gov.ons.fwmt.census.rmadapter.message.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import uk.gov.ons.fwmt.census.rmadapter.config.QueueConfig;
import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

@RunWith(MockitoJUnitRunner.class)
public class RMProducerImplTest {

  @InjectMocks
  private RMProducer rmProducer;

  @Mock
  private RabbitTemplate rabbitTemplate;

  @Mock
  private Exchange exchange;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void sendJobRequestResponse() throws CTPException {

    CensusCaseOutcomeDTO censusCaseOutcome = new CensusCaseOutcomeDTO();
    censusCaseOutcome.setCaseId("testId");
    censusCaseOutcome.setCaseReference("testRef");
    censusCaseOutcome.setOutcome("test");
    censusCaseOutcome.setOutcomeCategory("test");
    censusCaseOutcome.setOutcomeNote("test");

    String expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:CensusCaseOutcomeDTO xmlns:ns2=\"http://ons.gov.uk/fwmt/CensusCaseOutcomeDTO\"><caseId>testId</caseId><caseReference>testRef</caseReference><outcome>test</outcome><outcomeCategory>test</outcomeCategory><outcomeNote>test</outcomeNote></ns2:CensusCaseOutcomeDTO>";
    when(exchange.getName()).thenReturn(QueueConfig.GATEWAY_ACTIONS);
    rmProducer.sendJobRequestResponse(censusCaseOutcome);

    verify(rabbitTemplate).convertAndSend(eq(QueueConfig.GATEWAY_ACTIONS), eq(QueueConfig.RM_RESPONSE_ROUTING_KEY), eq(expectedResult));
  }
}
