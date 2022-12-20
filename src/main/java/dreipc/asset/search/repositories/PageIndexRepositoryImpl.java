package dreipc.asset.search.repositories;

import de.dreipc.q8r.asset.page.similarities.types.PageSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.PageSearchWhereInput;
import de.dreipc.q8r.asset.page.similarities.types.PageUniqueInput;
import dreipc.asset.search.graphql.FilterMiddlewareElasticsearch;
import dreipc.asset.search.models.PageIndex;
import dreipc.common.graphql.query.OffsetBasedRequest;
import dreipc.common.graphql.query.mongodb.SortMiddlewareMongoDB;
import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Slf4j
@Repository
public class PageIndexRepositoryImpl implements PageIndexRepositoryCustom {
    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SortMiddlewareMongoDB sortEngine;
    private final FilterMiddlewareElasticsearch filterEngine;

    private final List<String> queryFields;

    public PageIndexRepositoryImpl(ElasticsearchRestTemplate elasticsearchTemplate, SortMiddlewareMongoDB sortEngine, FilterMiddlewareElasticsearch filterEngine) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.sortEngine = sortEngine;
        this.filterEngine = filterEngine;
        this.queryFields = List.of("content", "namedEntities.literal", "tags");
    }

    @Override
    public CountConnection<PageIndex> searchPages(PageSearchWhereInput where, List<PageSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        var sorting = sortEngine.convert(orderBy, PageIndex.class);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        OffsetBasedRequest pageRequest = new OffsetBasedRequest(first, skip, sorting);

        try {
            boolQueryBuilder.must(findContains(where));
        } catch (Exception e) {
        }

        try {
            boolQueryBuilder.must(matchProjectQuery(where));
        } catch (Exception e) {
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .build();

        var searchHits = elasticsearchTemplate.search(searchQuery, PageIndex.class);
        var list = searchHits.stream().map(searchHit -> searchHit.getContent()).toList();
        return new CountConnection<>(list, searchHits.getTotalHits(), environment);
    }


    @Override
    public CountConnection<PageIndex> searchSimilarPages(PageUniqueInput where, List<PageSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {

        var sorting = sortEngine.convert(orderBy, PageIndex.class);
        OffsetBasedRequest pageRequest = new OffsetBasedRequest(first, skip, sorting);

        List<String> strings = new ArrayList<>();
        var query = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("id", where.getId()))
                .build();

        var page = elasticsearchTemplate.searchOne(query, PageIndex.class);
        strings.add(page.getContent().getContent());
        MoreLikeThisQueryBuilder moreLikeThisHtmlQueryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"content"}, strings.toArray(String[]::new), null)
                .maxQueryTerms(12)
                .minTermFreq(1)
                .boost(10);

        BoolQueryBuilder filter = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("id", page.getId()));

        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(filter)
                .withQuery(moreLikeThisHtmlQueryBuilder)
                .withPageable(pageRequest)
                .build();


        var searchHits = elasticsearchTemplate.search(searchQuery, PageIndex.class);
        var list = searchHits.stream().map(SearchHit::getContent).toList();
        return new CountConnection<>(list, list.size(), environment);
    }


    private TermQueryBuilder matchProjectQuery(PageSearchWhereInput where) {
        var projectId = where.getProject().getId().getEquals();
        return QueryBuilders.termQuery("projectId", projectId);
    }

    private SimpleQueryStringBuilder findContains(PageSearchWhereInput where) {
        if (where.getContent() == null || where.getContent().isBlank())
            throw new IllegalArgumentException("Can't perform contains query,contains value is blank or null");
        return new SimpleQueryStringBuilder(where.getContent()).fields(searchableFields());
    }


    private Map<String, Float> searchableFields() {
        return queryFields.stream()
                .collect(Collectors.toMap(s -> s, s -> 0f));
    }


}
