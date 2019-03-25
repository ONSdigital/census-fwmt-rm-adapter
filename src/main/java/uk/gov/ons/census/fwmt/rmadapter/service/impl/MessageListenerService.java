package uk.gov.ons.census.fwmt.rmadapter.service.impl;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerService {
  @Autowired SimpleMessageListenerContainer simpleMessageListenerContainer;

  public void startMessageListener() {
    simpleMessageListenerContainer.start();
  }

  public void stopMessageListener() {
    simpleMessageListenerContainer.stop();
  }
}
