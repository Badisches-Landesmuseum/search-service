package dreipc.asset.search.repositories;


import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchWhereInput;
import dreipc.asset.search.graphql.FilterMiddlewareElasticsearch;
import dreipc.asset.search.models.MuseumObjectCountConnection;
import dreipc.asset.search.models.MuseumObjectIndex;
import dreipc.asset.search.models.TopicIndex;
import dreipc.common.graphql.query.OffsetBasedRequest;
import dreipc.common.graphql.query.mongodb.SortMiddlewareMongoDB;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


@Slf4j
@Component
public class MuseumObjectIndexRepositoryImpl implements MuseumObjectIndexRepositoryCustom {
    private final ElasticsearchRestTemplate elasticsearchTemplate;

    private final SortMiddlewareMongoDB sortEngine;
    private final FilterMiddlewareElasticsearch filterEngine;


    public MuseumObjectIndexRepositoryImpl(ElasticsearchRestTemplate elasticsearchTemplate, SortMiddlewareMongoDB sortEngine, FilterMiddlewareElasticsearch filterEngine) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.sortEngine = sortEngine;
        this.filterEngine = filterEngine;
    }


    @Override
    public MuseumObjectCountConnection searchMuseumObjects(MuseumObjectSearchWhereInput where, List<MuseumObjectSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        var sorting = sortEngine.convert(orderBy, MuseumObjectIndex.class);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        OffsetBasedRequest pageRequest = new OffsetBasedRequest(first, skip, sorting);

        try {
            boolQueryBuilder.should(matchQuery(where.getKeywords(), "topics.topics"));
        } catch (Exception e) {
            log.info("Did not apply topics search");
        }

        try {
            boolQueryBuilder.must(simpleQuery(where.getKeywords()));
        } catch (Exception e) {
            log.info("Did not apply keywords search");
        }

        try {
            boolQueryBuilder.should(matchQuery(where.getEpochs(), "epoch"));
        } catch (Exception e) {
            log.info("Did not apply epoch search");
        }

        try {
            boolQueryBuilder.must(matchQuery(where.getMaterials(), "materials"));
        } catch (Exception e) {
            log.info("Did not apply materials search");
        }

        try {
            boolQueryBuilder.must(matchQuery(where.getCountries(), "countryName"));
        } catch (Exception e) {
            log.info("Did not apply country search");
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withTrackTotalHits(true)
                .withPageable(pageRequest)
                .build();

        var searchHits = elasticsearchTemplate.search(searchQuery, MuseumObjectIndex.class);
        var list = searchHits.stream().map(SearchHit::getContent).toList();

        var smartKeywords = new ArrayList<String>();
        smartKeywords.addAll(extractKeywords(list));
        smartKeywords.addAll(extractTopics(list));
        var keywords = smartKeywords.stream().limit(first).skip(skip).toList();

        return new MuseumObjectCountConnection(list, searchHits.getTotalHits(), environment, keywords);
    }

    @Override
    public MuseumObjectCountConnection<MuseumObjectIndex> exceptionalMuseumObjects(MuseumObjectSearchWhereInput where, List<MuseumObjectSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return searchMuseumObjects(where, orderBy, first, skip, environment);
    }

    @Override
    public List<String> smartKeywords(int first, int skip) {
        String[] includes = {"keywords", "topics.topics"};

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFields(includes)
                .withSourceFilter(new FetchSourceFilter(includes, null))
                .build();

        var searchHits = elasticsearchTemplate.search(searchQuery, MuseumObjectIndex.class);
        var list = searchHits.stream().map(SearchHit::getContent).toList();

        var smartKeywords = new ArrayList<String>();
        smartKeywords.addAll(extractKeywords(list));
        smartKeywords.addAll(extractTopics(list));
        return smartKeywords.stream().limit(first).skip(skip).toList();
    }


    private List<String> extractTopics(List<MuseumObjectIndex> museumObjects) {
        try {
            return museumObjects
                    .stream()
                    .map(MuseumObjectIndex::getTopics)
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .map(TopicIndex::getTopics)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet())
                    .stream()
                    .toList();

        } catch (Exception e) {
            log.error("Error extracting topics", e);
            return Collections.emptyList();
        }

    }

    private List<String> extractKeywords(List<MuseumObjectIndex> museumObjectIndices) {

        return museumObjectIndices
                .stream()
                .map(MuseumObjectIndex::getKeywords)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

    private BoolQueryBuilder matchQuery(List<String> terms, String field) {
        if (CollectionUtils.isEmpty(terms)) throw new NullPointerException();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        terms.forEach(keyword -> boolQueryBuilder.should(QueryBuilders.matchQuery(field, keyword)));
        return boolQueryBuilder;
    }


    private BoolQueryBuilder simpleQuery(List<String> terms) {
        if (CollectionUtils.isEmpty(terms)) throw new NullPointerException();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        terms.forEach(keyword -> boolQueryBuilder.should(new SimpleQueryStringBuilder(keyword)));
        return boolQueryBuilder;
    }

}
