package com.github.springkafkasamples.producerconsumersample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Spring BootでEmbeddedKafkaを使ったテストをする
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {
    "listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class EmbeddedKafkaSpringBootTest {

  @Autowired
  private KafkaProducer kafkaProducer;
  @Autowired
  private KafkaConsumer kafkaConsumer;
  @Value("${test.topic}")
  private String topic;

  @Test
  void test() throws InterruptedException {
    String data = "Sending with our own simple KafkaProducer";

    kafkaProducer.send(topic, data);

    boolean messageConsumed = kafkaConsumer.getLatch().await(10, TimeUnit.SECONDS);
    assertTrue(messageConsumed);
    assertThat(kafkaConsumer.getPayload()).contains(data);
  }
}
