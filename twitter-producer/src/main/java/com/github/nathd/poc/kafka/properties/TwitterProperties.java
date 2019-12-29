package com.github.nathd.poc.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.social.twitter")
@Getter
@Setter
public class TwitterProperties {
    private String appId;
    private String appSecret;
    private String accessToken;
    private String accessSecret;
}
