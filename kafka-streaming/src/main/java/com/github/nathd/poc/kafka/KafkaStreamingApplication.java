package com.github.nathd.poc.kafka;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaStreamingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
