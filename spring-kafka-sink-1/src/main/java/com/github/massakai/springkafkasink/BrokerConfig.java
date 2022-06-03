package com.github.massakai.springkafkasink;

import java.util.List;
import lombok.Data;

@Data
public class BrokerConfig {

  private List<String> bootstrapServers;

}
