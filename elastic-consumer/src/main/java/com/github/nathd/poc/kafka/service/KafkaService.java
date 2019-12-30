package com.github.nathd.poc.kafka.service;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

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
            int recordCount = records.count();
            log.info("Received {} records", recordCount);
            List<IndexQuery> queryList = new LinkedList<>();
            for(ConsumerRecord<String, String> record: records) {
                try {
                    String id = extractId(record.value());
                    queryList.add(elasticSearchService.prepareIndexQuery(id, record.value()));
                } catch (Exception e) {
                    log.warn("Ignored bad data {} of partition {} and offset {}",
                            record.value(), record.partition(), record.offset());
                    e.printStackTrace();
                }
            }
            if (queryList.size() > 0) {
                elasticSearchService.saveInBulk(queryList);
                log.info("Committing offsets...");
                kafkaConsumer.commitSync();
                log.info("Offsets have been committed");
            }
        }
    }

    private String extractId(String record) {
        return jsonParser.parse(record).getAsJsonObject().get("id").getAsString();
    }
}
