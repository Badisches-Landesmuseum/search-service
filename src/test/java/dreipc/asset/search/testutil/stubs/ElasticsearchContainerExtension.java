package dreipc.asset.search.testutil.stubs;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@Slf4j
public class ElasticsearchContainerExtension implements Extension, BeforeAllCallback, AfterAllCallback {

    private final ElasticsearchContainer container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.16.3");

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        container.start();
        var client = RestClient
                .builder(HttpHost.create(container.getHttpHostAddress()))
                .build();
        Response response = client.performRequest(new Request("GET", "/_cluster/health"));
        System.setProperty("spring.data.elasticsearch.port", getPort().toString());
    }

    Integer getPort() {
        return container.getMappedPort(9200);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        container.close();
    }


}
