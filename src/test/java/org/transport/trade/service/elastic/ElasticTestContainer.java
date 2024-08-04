package org.transport.trade.service.elastic;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

class ElasticTestContainer extends ElasticsearchContainer {

    private static final String DOCKER_ELASTIC = "docker.elastic.co/elasticsearch/elasticsearch:7.17.6";

    private static final String CLUSTER_NAME = "sample-cluster";

    private static final String ELASTIC_SEARCH = "elasticsearch";

    public ElasticTestContainer() {
        super(DOCKER_ELASTIC);
        this.addFixedExposedPort(9200, 9200);
        //        this.addFixedExposedPort(9301, 9301);
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }
}
