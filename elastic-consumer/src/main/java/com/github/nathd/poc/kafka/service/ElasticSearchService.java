package com.github.nathd.poc.kafka.service;

import com.github.nathd.poc.kafka.properties.ElasticSearchProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticSearchProperty elasticSearchProperty;

    public void save(String uniqueId, String tweet) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withIndexName(elasticSearchProperty.getIndex())
                .withType(elasticSearchProperty.getType())
                .withId(uniqueId) // this is to make our consumer idempotent
                .withSource(tweet).build();

        String documentId = elasticsearchOperations.index(indexQuery);
        log.info("ID : {}", documentId);
    }
}
