package com.github.nathd.poc.kafka.config;

import com.github.nathd.poc.kafka.properties.ElasticSearchProperty;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient(ElasticSearchProperty elasticSearchProperty) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(elasticSearchProperty.getUserKey(),
                        elasticSearchProperty.getUserSecret()));

        RestClientBuilder builder = RestClient
                .builder(new HttpHost(elasticSearchProperty.getHostname(), 443, "https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations(RestHighLevelClient restHighLevelClient) {
        return new ElasticsearchRestTemplate(restHighLevelClient);
    }

    @Bean
    public KafkaConsumer kafkaConsumer(KafkaProperties kafkaProperties) {
        KafkaConsumer<String, String> kafkaConsumer =
                new KafkaConsumer<String, String>(kafkaProperties.getConsumer().buildProperties());
        kafkaConsumer.subscribe(Arrays.asList("twitter"));
        return kafkaConsumer;
    }
}
