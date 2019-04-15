package uk.gov.ons.census.fwmt.rmadapter.controller;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QueueListenerController {

  @Autowired SimpleMessageListenerContainer simpleMessageListenerContainer;

  @GetMapping("/startListener")
  public ResponseEntity startListener() {
    simpleMessageListenerContainer.start();
    return ResponseEntity.ok("Queue listener started.");
  }

  @GetMapping("/stopListener")
  public ResponseEntity stopListener() {
    simpleMessageListenerContainer.stop();
    return ResponseEntity.ok("Queue listener stopped.");
  }
}
