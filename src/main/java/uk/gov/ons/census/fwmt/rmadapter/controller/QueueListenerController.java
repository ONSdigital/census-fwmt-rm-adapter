package uk.gov.ons.census.fwmt.rmadapter.controller;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QueueListenerController {
  @Autowired SimpleMessageListenerContainer simpleMessageListenerContainer;

  @GetMapping("/startListener")
  public String startListener() {
    simpleMessageListenerContainer.start();
    return "Listener started";
  }

  @GetMapping("/stopListener")
  public String stopListener() {
    simpleMessageListenerContainer.stop();
    return "Listener stopped";
  }
}
