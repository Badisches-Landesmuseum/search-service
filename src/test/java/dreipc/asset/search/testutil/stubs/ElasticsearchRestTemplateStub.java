package dreipc.asset.search.testutil.stubs;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.cluster.ClusterOperations;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.core.routing.RoutingResolver;

import java.util.List;

public class ElasticsearchRestTemplateStub extends ElasticsearchRestTemplate {

    public ElasticsearchRestTemplateStub(RestHighLevelClient client) {
        super(client);
    }
}
