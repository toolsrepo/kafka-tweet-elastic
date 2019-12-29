package com.github.nathd.poc.kafka.service;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {

    private final KafkaConsumer<String, String> kafkaConsumer;
    private final ElasticSearchService elasticSearchService;
    private final JsonParser jsonParser = new JsonParser();
    public void consumeAndForward() {

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            log.info("Received {} records", records.count());
            for(ConsumerRecord<String, String> record: records) {
                String id = extractId(record.value());
                elasticSearchService.save(id, record.value());
            }
            log.info("Committing offsets...");
            kafkaConsumer.commitSync();
            log.info("Offsets have been committed");
        }
    }

    private String extractId(String record) {
        return jsonParser.parse(record).getAsJsonObject().get("id_str").getAsString();
    }

}
