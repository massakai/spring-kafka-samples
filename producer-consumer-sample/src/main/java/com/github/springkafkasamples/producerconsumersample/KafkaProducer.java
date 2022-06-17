package com.github.springkafkasamples.producerconsumersample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaProducer(final KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(final String topic, final String payload) {
    log.info("sending payload='{}' to topic='{}'", payload, topic);
    kafkaTemplate.send(topic, payload);
  }
}
