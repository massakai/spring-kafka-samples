package com.github.massakai.springkafkasink.topic1;

import com.github.massakai.springkafkasink.BrokerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

@Configuration
public class ConsumerConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "broker")
  BrokerConfig brokerConfig() {
    return new BrokerConfig();
  }

  @Bean
  KafkaMessageListenerContainer<Integer, String> kafkaMessageListenerContainer(
      final ConsumerFactory<Integer, String> consumerFactory) {
    var containerProperties = new ContainerProperties("topic1");
    containerProperties.setMessageListener(new TopicListener());
    return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
  }

  @Bean
  public ConsumerFactory<Integer, String> consumerFactory(final BrokerConfig brokerConfig) {
    return new DefaultKafkaConsumerFactory<>(consumerProperties(brokerConfig));
  }

  private Map<String, Object> consumerProperties(final BrokerConfig brokerConfig) {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerConfig.getBootstrapServers());
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return properties;
  }
}
