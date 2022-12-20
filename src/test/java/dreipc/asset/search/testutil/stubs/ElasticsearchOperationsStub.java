package dreipc.asset.search.testutil.stubs;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.cluster.ClusterOperations;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.core.reindex.ReindexRequest;
import org.springframework.data.elasticsearch.core.reindex.ReindexResponse;
import org.springframework.data.elasticsearch.core.routing.RoutingResolver;

import java.util.List;

public class ElasticsearchOperationsStub implements ElasticsearchOperations {
    @Override
    public IndexOperations indexOps(Class<?> clazz) {
        return null;
    }

    @Override
    public IndexOperations indexOps(IndexCoordinates index) {
        return null;
    }

    @Override
    public ClusterOperations cluster() {
        return null;
    }

    @Override
    public ElasticsearchConverter getElasticsearchConverter() {
        return null;
    }

    @Override
    public IndexCoordinates getIndexCoordinatesFor(Class<?> clazz) {
        return null;
    }

    @Override
    public String getEntityRouting(Object entity) {
        return null;
    }

    @Override
    public ElasticsearchOperations withRouting(RoutingResolver routingResolver) {
        return null;
    }

    @Override
    public <T> T save(T entity) {
        return null;
    }

    @Override
    public <T> T save(T entity, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> Iterable<T> save(Iterable<T> entities) {
        return null;
    }

    @Override
    public <T> Iterable<T> save(Iterable<T> entities, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> Iterable<T> save(T... entities) {
        return null;
    }

    @Override
    public String index(IndexQuery query, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> T get(String id, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T get(String id, Class<T> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> List<MultiGetItem<T>> multiGet(Query query, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<MultiGetItem<T>> multiGet(Query query, Class<T> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public boolean exists(String id, Class<?> clazz) {
        return false;
    }

    @Override
    public boolean exists(String id, IndexCoordinates index) {
        return false;
    }

    @Override
    public List<IndexedObjectInformation> bulkIndex(List<IndexQuery> queries, BulkOptions bulkOptions, Class<?> clazz) {
        return null;
    }

    @Override
    public List<IndexedObjectInformation> bulkIndex(List<IndexQuery> queries, BulkOptions bulkOptions, IndexCoordinates index) {
        return null;
    }

    @Override
    public void bulkUpdate(List<UpdateQuery> queries, Class<?> clazz) {

    }

    @Override
    public void bulkUpdate(List<UpdateQuery> queries, BulkOptions bulkOptions, IndexCoordinates index) {

    }

    @Override
    public String delete(String id, IndexCoordinates index) {
        return null;
    }

    @Override
    public String delete(String id, String routing, IndexCoordinates index) {
        return null;
    }

    @Override
    public String delete(String id, Class<?> entityType) {
        return null;
    }

    @Override
    public String delete(Object entity) {
        return null;
    }

    @Override
    public String delete(Object entity, IndexCoordinates index) {
        return null;
    }

    @Override
    public ByQueryResponse delete(Query query, Class<?> clazz) {
        return null;
    }

    @Override
    public ByQueryResponse delete(Query query, Class<?> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public UpdateResponse update(UpdateQuery updateQuery, IndexCoordinates index) {
        return null;
    }

    @Override
    public ByQueryResponse updateByQuery(UpdateQuery updateQuery, IndexCoordinates index) {
        return null;
    }

    @Override
    public ReindexResponse reindex(ReindexRequest reindexRequest) {
        return null;
    }

    @Override
    public String submitReindex(ReindexRequest reindexRequest) {
        return null;
    }

    @Override
    public long count(Query query, Class<?> clazz) {
        return 0;
    }

    @Override
    public long count(Query query, Class<?> clazz, IndexCoordinates index) {
        return 0;
    }

    @Override
    public SearchResponse suggest(SuggestBuilder suggestion, Class<?> clazz) {
        return null;
    }

    @Override
    public SearchResponse suggest(SuggestBuilder suggestion, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> List<SearchHits<T>> multiSearch(List<? extends Query> queries, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<SearchHits<T>> multiSearch(List<? extends Query> queries, Class<T> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public List<SearchHits<?>> multiSearch(List<? extends Query> queries, List<Class<?>> classes) {
        return null;
    }

    @Override
    public List<SearchHits<?>> multiSearch(List<? extends Query> queries, List<Class<?>> classes, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> SearchHits<T> search(Query query, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> SearchHits<T> search(Query query, Class<T> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> SearchHits<T> search(MoreLikeThisQuery query, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> SearchHits<T> search(MoreLikeThisQuery query, Class<T> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public <T> SearchHitsIterator<T> searchForStream(Query query, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> SearchHitsIterator<T> searchForStream(Query query, Class<T> clazz, IndexCoordinates index) {
        return null;
    }

    @Override
    public Query matchAllQuery() {
        return null;
    }

    @Override
    public Query idsQuery(List<String> ids) {
        return null;
    }
}
