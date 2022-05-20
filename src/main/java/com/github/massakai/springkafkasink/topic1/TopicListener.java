package com.github.massakai.springkafkasink.topic1;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

@Slf4j
public class TopicListener implements MessageListener<Integer, String> {

  @Override
  public void onMessage(ConsumerRecord<Integer, String> data) {
    log.info("Message was received. value = {}", data.value());
  }
}
