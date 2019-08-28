package uk.gov.ons.census.fwmt.rmadapter.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QueueListenerControllerTest {

  @InjectMocks
  private QueueListenerController queueListenerController;

  @Mock
  private SimpleMessageListenerContainer simpleMessageListenerContainer;

  @Test
  public void testStartQueueListenerController() throws Exception {

    ResponseEntity responseEntity = queueListenerController.startListener();

    assertEquals("Queue listener started.", responseEntity.getBody());
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  public void testStopQueueListenerController() throws Exception {

    ResponseEntity responseEntity = queueListenerController.stopListener();

    assertEquals("Queue listener stopped.", responseEntity.getBody());
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

}
