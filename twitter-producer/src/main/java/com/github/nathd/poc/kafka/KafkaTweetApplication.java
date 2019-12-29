package com.github.nathd.poc.kafka;

import com.github.nathd.poc.kafka.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class KafkaTweetApplication implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private TwitterService twitterService;

    public static void main(String[] args) {
        SpringApplication.run(KafkaTweetApplication.class, args);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        twitterService.stopConsumption();
    }
}
