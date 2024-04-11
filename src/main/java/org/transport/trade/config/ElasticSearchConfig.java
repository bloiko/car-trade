package org.transport.trade.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    private static final String SERVER_URL = "http://localhost:9200"; // TODO move to property

    private static final String ES_USERNAME = "elastic"; // TODO move to property

    private static final String ES_PASSWORD = "elastic"; // TODO move to property

    @Bean
    public RestClient restClient() {
        RestClientBuilder builder = RestClient.builder(HttpHost.create(SERVER_URL));
        builder.setDefaultHeaders(new Header[]{new BasicHeader(ES_USERNAME, ES_PASSWORD)});
        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport elasticsearchTransport) {
        return new ElasticsearchClient(elasticsearchTransport);
    }
}
