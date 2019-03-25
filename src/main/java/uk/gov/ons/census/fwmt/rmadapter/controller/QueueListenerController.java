package uk.gov.ons.census.fwmt.rmadapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.ons.census.fwmt.rmadapter.service.impl.MessageListenerService;

@Controller
public class QueueListenerController {

  @Autowired MessageListenerService messageListenerService;

  @GetMapping("/startListener")
  public String startListener() {
    messageListenerService.startMessageListener();
    return "Listener started";
  }

  @GetMapping("/stopListener")
  public String stopListener() {
    messageListenerService.stopMessageListener();
    return "Listener stopped";
  }
}
