package uk.gov.ons.fwmt.census.rmadapter.message.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.rmadapter.data.DummyRMReturn;
import uk.gov.ons.fwmt.census.rmadapter.message.impl.RMProducerImpl;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueNames;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;
import uk.gov.ons.fwmt.noncontactdetail.NonContactDetail;
import uk.gov.ons.fwmt.propertydetails.PropertyDetails;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RMProducerImplTest {

  @InjectMocks
  private RMProducerImpl rmProducer;

  @Mock
  private RabbitTemplate rabbitTemplate;

  @Mock
  private Exchange exchange;

  @Captor
  private ArgumentCaptor argumentCaptor;

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
    when(exchange.getName()).thenReturn(QueueNames.RM_JOB_SVC_EXCHANGE);
    rmProducer.sendJobRequestResponse(censusCaseOutcome);

    verify(rabbitTemplate).convertAndSend(eq(QueueNames.RM_JOB_SVC_EXCHANGE),eq(QueueNames.RM_RESPONSE_ROUTING_KEY), argumentCaptor.capture());
    String result = String.valueOf(argumentCaptor.getValue());

    assertEquals(expectedResult, result);
  }
}
