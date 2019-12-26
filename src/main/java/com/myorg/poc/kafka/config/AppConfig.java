package com.myorg.poc.kafka.config;

import com.myorg.poc.kafka.properties.TwitterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class AppConfig {

    @Bean
    public Twitter twitter(TwitterProperties twitterProperties) {
        return new TwitterTemplate(twitterProperties.getAppId(), twitterProperties.getAppSecret(),
                twitterProperties.getAccessToken(), twitterProperties.getAccessSecret());
    }
}
