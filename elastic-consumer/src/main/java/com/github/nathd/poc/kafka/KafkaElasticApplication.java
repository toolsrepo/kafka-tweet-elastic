package com.github.nathd.poc.kafka;

import com.github.nathd.poc.kafka.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaElasticApplication implements CommandLineRunner {

    @Autowired
    private ElasticSearchService elasticSearchService;

    public static void main(String[] args) {
        SpringApplication.run(KafkaElasticApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        elasticSearchService.saveUsingOperations();
    }
}
