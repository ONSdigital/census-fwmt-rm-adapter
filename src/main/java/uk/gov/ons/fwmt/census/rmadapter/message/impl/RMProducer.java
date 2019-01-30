package uk.gov.ons.fwmt.census.rmadapter.message.impl;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.census.rmadapter.config.QueueConfig;
import uk.gov.ons.fwmt.census.rmadapter.data.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

@Component
@Slf4j
public class RMProducer {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private Exchange exchange;

  @Retryable
  public void sendJobRequestResponse(CensusCaseOutcomeDTO censusCaseOutcome) throws CTPException {
    JAXBContext jaxbContext;
    try {
      jaxbContext = JAXBContext.newInstance(CensusCaseOutcomeDTO.class);
      Marshaller marshaller = jaxbContext.createMarshaller();

      StringWriter sw = new StringWriter();

      QName qName = new QName("http://ons.gov.uk/fwmt/CensusCaseOutcomeDTO", "CensusCaseOutcomeDTO");
      JAXBElement<CensusCaseOutcomeDTO> root = new JAXBElement<CensusCaseOutcomeDTO>(qName, CensusCaseOutcomeDTO.class,
          censusCaseOutcome);
      marshaller.marshal(root, sw);
      String rmJobRequestResponse = sw.toString();

      rabbitTemplate.convertAndSend(exchange.getName(), QueueConfig.RM_RESPONSE_ROUTING_KEY, rmJobRequestResponse);
      log.info("Sent job response to RM");
    } catch (JAXBException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Failed to convert and send to RM.", e);
    }
  }
}
