package com.github.nathd.poc.kafka.service;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TweetListener {

    private final ElasticSearchService elasticSearchService;
    private final JsonParser jsonParser = new JsonParser();

    @KafkaListener(topics = "twitter", groupId = "my-group",
            clientIdPrefix = "consumer-2", containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMessage(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        int recordCount = records.size();
        log.info("Received {} records", recordCount);
        List<IndexQuery> queryList = new LinkedList<>();
        for(ConsumerRecord<String, String> record: records) {
            try {
                String id = extractId(record.value());
                queryList.add(elasticSearchService.prepareIndexQuery(id, record.value()));
            } catch (Exception e) {
                log.warn("Ignored bad data {} of partition {} and offset {}",
                        record.value(), record.partition(), record.offset());
            }
        }
        if (recordCount > 0) {
            elasticSearchService.saveInBulk(queryList);
            log.info("Committing offsets...");
            ack.acknowledge();
            log.info("Offsets have been committed");
        }
    }

    private String extractId(String record) {
        return jsonParser.parse(record).getAsJsonObject().get("id").getAsString();
    }
}
