package com.myorg.poc.kafka.service;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaProperties kafkaProperties) {
        this.kafkaTemplate = new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(kafkaProperties.getProducer().buildProperties()), true
        );
    }

    public void send(String topic, String tweets) {
        kafkaTemplate.send(topic, tweets);
    }
}
