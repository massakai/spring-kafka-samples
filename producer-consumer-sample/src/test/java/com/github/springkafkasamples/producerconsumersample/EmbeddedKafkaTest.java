package com.github.springkafkasamples.producerconsumersample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.test.context.EmbeddedKafka;

/**
 * Spring BootなしでEmbeddedKafkaを使ったテストをする
 */
@EmbeddedKafka(partitions = 1, brokerProperties = {
    "listeners=PLAINTEXT://localhost:9092",
    "port=9092"})
public class EmbeddedKafkaTest {

  @Configuration
  @ComponentScan
  @PropertySource("classpath:embedded-kafka-test.properties")
  static class EmbeddedKafkaTestConfiguration {

    @Value("${test.topic}")
    private String topic;

    public String getTopic() {
      return topic;
    }

  }

  @Test
  void test() throws InterruptedException {
    try (var context = new AnnotationConfigApplicationContext(
        EmbeddedKafkaTestConfiguration.class)) {
      KafkaProducer kafkaProducer = context.getBean(KafkaProducer.class);
      KafkaConsumer kafkaConsumer = context.getBean(KafkaConsumer.class);
      EmbeddedKafkaTestConfiguration embeddedKafkaTestConfiguration = context.getBean(
          EmbeddedKafkaTestConfiguration.class);

      String topic = embeddedKafkaTestConfiguration.getTopic();
      String data = "Sending with our own simple KafkaProducer";

      kafkaProducer.send(topic, data);

      boolean messageConsumed = kafkaConsumer.getLatch().await(10, TimeUnit.SECONDS);
      assertTrue(messageConsumed);
      assertThat(kafkaConsumer.getPayload()).contains(data);
    }
  }

  @Test
  void testUsingApplicationContextRunner() {
    new ApplicationContextRunner()
        .withUserConfiguration(EmbeddedKafkaTestConfiguration.class)
        .run(context -> {
          KafkaProducer kafkaProducer = context.getBean(KafkaProducer.class);
          KafkaConsumer kafkaConsumer = context.getBean(KafkaConsumer.class);
          EmbeddedKafkaTestConfiguration embeddedKafkaTestConfiguration = context.getBean(
              EmbeddedKafkaTestConfiguration.class);

          String topic = embeddedKafkaTestConfiguration.getTopic();
          String data = "Sending with our own simple KafkaProducer";

          kafkaProducer.send(topic, data);

          boolean messageConsumed = kafkaConsumer.getLatch().await(10, TimeUnit.SECONDS);
          assertTrue(messageConsumed);
          assertThat(kafkaConsumer.getPayload()).contains(data);
        });
  }

}
