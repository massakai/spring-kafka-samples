package com.github.massakai.springkafkasink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringKafkaSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaSinkApplication.class, args);
	}

}
