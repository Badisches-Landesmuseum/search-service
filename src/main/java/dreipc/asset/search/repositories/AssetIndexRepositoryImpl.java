package dreipc.asset.search.repositories;


import de.dreipc.q8r.asset.page.similarities.types.*;
import dreipc.asset.search.graphql.FilterMiddlewareElasticsearch;
import dreipc.asset.search.models.AssetIndex;
import dreipc.asset.search.models.PanquraArticleIndexSearchResult;
import dreipc.common.graphql.query.OffsetBasedRequest;
import dreipc.common.graphql.query.mongodb.SortMiddlewareMongoDB;
import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;


@Slf4j
@Component
public class AssetIndexRepositoryImpl implements AssetIndexRepositoryCustom {
    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final SortMiddlewareMongoDB sortEngine;
    private final FilterMiddlewareElasticsearch filterEngine;
    private final List<String> queryFields;

    public AssetIndexRepositoryImpl(ElasticsearchRestTemplate elasticsearchTemplate, SortMiddlewareMongoDB sortEngine, FilterMiddlewareElasticsearch filterEngine) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.sortEngine = sortEngine;
        this.filterEngine = filterEngine;
        this.queryFields = List.of("fileName", "shortAbstract", "title", "content");
    }

    @Override
    public CountConnection<AssetIndex> searchAssets(AssetSearchWhereInput where, List<AssetSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {

        var boolQueryBuilder = filterEngine.convert(where, AssetIndex.class);

        try {
            boolQueryBuilder.must(findContains(where));
        } catch (Exception e) {
        }

        try {
            boolQueryBuilder.must(findStartsWith(where));
        } catch (Exception e) {
        }

        try {
            boolQueryBuilder.filter(matchProjectQuery(where));
        } catch (Exception e) {
        }

        var sorting = sortEngine.convert(orderBy, AssetIndex.class);
        OffsetBasedRequest pageRequest = new OffsetBasedRequest(first, skip, sorting);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withHighlightFields(getHighlighters(List.of("fileName", "shortAbstract", "title", "content")))
                .build();


        var searchHits = elasticsearchTemplate.search(searchQuery, AssetIndex.class);
        var totalCount = elasticsearchTemplate.count(searchQuery, AssetIndex.class);
        var list = searchHits.stream().map(SearchHit::getContent).toList();
        return new CountConnection<>(list, totalCount, environment);
    }

    @Override
    public CountConnection<PanquraArticleIndexSearchResult> searchWithHighlights(PanquraArticleSearchWhereInput where, List<PanquraArticleOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        var boolQueryBuilder = filterEngine.convert(where, AssetIndex.class);

        try {
            boolQueryBuilder.must(findContains(where));
        } catch (Exception e) {
        }

        try {
            boolQueryBuilder.must(findStartsWith(where));
        } catch (Exception e) {
        }

        try {
            boolQueryBuilder.filter(matchProjectQuery(where));
        } catch (Exception e) {
        }

        var sorting = sortEngine.convert(orderBy, AssetIndex.class);
        OffsetBasedRequest pageRequest = new OffsetBasedRequest(first, skip, sorting);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withHighlightFields(getHighlighters(List.of("fileName", "shortAbstract", "title", "content")))
                .build();


        var searchHits = elasticsearchTemplate.search(searchQuery, AssetIndex.class);
        var list = searchHits.stream().map(searchHit -> PanquraArticleIndexSearchResult.builder()
                .highlights(filterEngine.toHighlights(searchHit.getHighlightFields()))
                .index(searchHit.getContent())
                .build()
        ).toList();
        return new CountConnection<>(list, searchHits.getTotalHits(), environment);
    }


    private TermQueryBuilder matchProjectQuery(AssetSearchWhereInput where) {
        var projectId = where.getProject().getId().getEquals();
        return QueryBuilders.termQuery("projectId", projectId);
    }

    private TermQueryBuilder matchProjectQuery(PanquraArticleSearchWhereInput where) {
        var projectId = where.getProject().getId().getEquals();
        return QueryBuilders.termQuery("projectId", projectId);
    }


    private List getHighlighters(List<String> highlightedFields) {
        return highlightedFields.stream().map(fieldName -> new HighlightBuilder.Field(fieldName)).toList();
    }

    private MultiMatchQueryBuilder findStartsWith(AssetSearchWhereInput where) {
        if (where.getContent().getStartsWith() == null || where.getContent().getStartsWith().isBlank())
            throw new IllegalArgumentException("Can't perform startsWith query,filter value blank or null");

        return multiMatchQuery(where.getContent().getStartsWith())
                .fields(searchableFields())
                .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
    }

    private MultiMatchQueryBuilder findStartsWith(PanquraArticleSearchWhereInput where) {
        if (where.getContent().getStartsWith() == null || where.getContent().getStartsWith().isBlank())
            throw new IllegalArgumentException("Can't perform startsWith query,filter value blank or null");

        return multiMatchQuery(where.getContent().getStartsWith())
                .fields(searchableFields())
                .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
    }


    private SimpleQueryStringBuilder findContains(AssetSearchWhereInput where) {
        if (where.getContent().getContains() == null || where.getContent().getContains().isBlank())
            throw new IllegalArgumentException("Can't perform contains query, ,contains value is blank or null");
        return new SimpleQueryStringBuilder(where.getContent().getContains())
                .fields(searchableFields())
                .defaultOperator(Operator.AND);
    }

    private SimpleQueryStringBuilder findContains(PanquraArticleSearchWhereInput where) {
        if (where.getContent().getContains() == null || where.getContent().getContains().isBlank())
            throw new IllegalArgumentException("Can't perform contains query, ,contains value is blank or null");
        return new SimpleQueryStringBuilder(where.getContent().getContains())
                .fields(searchableFields())
                .defaultOperator(Operator.AND);
    }


    private Map<String, Float> searchableFields() {
        return queryFields.stream()
                .collect(Collectors.toMap(s -> s, s -> 0f));
    }


}
