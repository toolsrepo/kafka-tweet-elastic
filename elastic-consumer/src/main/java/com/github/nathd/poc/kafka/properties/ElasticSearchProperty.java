package com.github.nathd.poc.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
public class ElasticSearchProperty {
    private String hostname;
    private String userKey;
    private String userSecret;
    private String index;
    private String type;
}
