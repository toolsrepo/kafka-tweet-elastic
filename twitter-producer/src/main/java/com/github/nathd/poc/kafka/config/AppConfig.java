package com.github.nathd.poc.kafka.config;

import com.github.nathd.poc.kafka.properties.TwitterProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class AppConfig {

    @Bean
    public Twitter twitter(TwitterProperties twitterProperties) {
        return new TwitterTemplate(twitterProperties.getAppId(), twitterProperties.getAppSecret(),
                twitterProperties.getAccessToken(), twitterProperties.getAccessSecret());
    }

    @Bean
    public KafkaTemplate kafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(kafkaProperties.getProducer().buildProperties()), true
        );
    }
}
