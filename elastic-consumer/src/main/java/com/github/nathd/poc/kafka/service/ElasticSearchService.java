package com.github.nathd.poc.kafka.service;

import com.github.nathd.poc.kafka.properties.ElasticSearchProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticSearchProperty elasticSearchProperty;
    private final RestHighLevelClient restHighLevelClient;

    public void save() throws IOException {
        String jsonString = "{\"foo\": \"bar\"}";
        IndexRequest indexRequest = new IndexRequest(elasticSearchProperty.getIndex(),
                elasticSearchProperty.getType()).source(jsonString, XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        String id = response.getId();
        log.info("ID : {}", id);

    }

    public void saveUsingOperations(){
        String jsonString = "{\"foo2\": \"bar2\"}";
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withIndexName(elasticSearchProperty.getIndex())
                .withType(elasticSearchProperty.getType())
                .withSource(jsonString).build();

        String documentId = elasticsearchOperations.index(indexQuery);
        log.info("ID : {}", documentId);
    }

}
