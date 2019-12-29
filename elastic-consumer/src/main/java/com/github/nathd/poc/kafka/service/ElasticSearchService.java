package com.github.nathd.poc.kafka.service;

import com.github.nathd.poc.kafka.properties.ElasticSearchProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ElasticSearchProperty elasticSearchProperty;

    public void save(String uniqueId, String tweet) {
        IndexQuery indexQuery = prepareIndexQuery(uniqueId, tweet);
        String documentId = elasticsearchRestTemplate.index(indexQuery);
        log.info("ID : {}", documentId);
    }

    public void saveInBulk(List<IndexQuery> bulkRequest) {
        elasticsearchRestTemplate.bulkIndex(bulkRequest);
    }

    public IndexQuery prepareIndexQuery(String uniqueId, String tweet) {
        return new IndexQueryBuilder()
                    .withIndexName(elasticSearchProperty.getIndex())
                    .withType(elasticSearchProperty.getType())
                    .withId(uniqueId) // this is to make our consumer idempotent
                    .withSource(tweet).build();
    }
}
