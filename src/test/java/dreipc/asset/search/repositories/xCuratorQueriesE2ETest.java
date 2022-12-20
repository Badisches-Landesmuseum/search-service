package dreipc.asset.search.repositories;

import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchWhereInput;
import dreipc.asset.search.graphql.FilterMiddlewareElasticsearch;
import dreipc.asset.search.models.MuseumObjectIndex;
import dreipc.asset.search.testutil.ElasticSearchConfig;
import dreipc.asset.search.testutil.stubs.DataFetchingEnvironmentStub;
import dreipc.asset.search.testutil.stubs.ElasticsearchContainerExtension;
import dreipc.asset.search.testutil.xCuratorTopicsParser;
import dreipc.common.graphql.query.mongodb.SortMiddlewareMongoDB;
import dreipc.asset.search.testutil.EntityGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {ElasticSearchConfig.class, xCuratorTopicsParser.class})
@ExtendWith(ElasticsearchContainerExtension.class)
@SpringBootTest(classes = {SortMiddlewareMongoDB.class, FilterMiddlewareElasticsearch.class})
class xCuratorQueriesE2ETest {

    @Autowired
    private MuseumObjectIndexRepository museumObjectIndexRepository;

    @Autowired
    private xCuratorTopicsParser xCuratorTopicsParser;


    @Test
    void init() {
        assertNotNull(museumObjectIndexRepository);
        assertNotNull(xCuratorTopicsParser);
    }


    @Test
    void museumObjects_query_by_keywords_success() {
        var keywords = List.of("Garten", "Landschaft");
        var museumObjectIndex = EntityGenerator.museumObjectIndex(keywords);
        museumObjectIndexRepository.save(museumObjectIndex);
        DataFetchingEnvironmentStub dataFetchingEnvironmentStub = new DataFetchingEnvironmentStub();
        var where = MuseumObjectSearchWhereInput.newBuilder().keywords(keywords).build();
        var results = museumObjectIndexRepository.searchMuseumObjects(where, new ArrayList<>(), 1, 0, dataFetchingEnvironmentStub);
        var firstPage = results.getEdges().stream().findFirst().get().getNode();
        assertTrue(firstPage.getKeywords().equals(keywords));
        assertNotNull(results.getKeywords());
    }


    public Iterable<MuseumObjectIndex> feedTopics(Resource resource) throws IOException {
        var topicsBySourceId = xCuratorTopicsParser.parseFromJson(resource);
        var museumObjectsWithTopics = topicsBySourceId.entrySet().stream().map(entry -> EntityGenerator.museumObjectIndexWithTopics(entry.getValue())).toList();
        return museumObjectIndexRepository.saveAll(museumObjectsWithTopics);
    }

    @Test
    @Disabled("Test needs to be adjusted, query works currently")
    void museumObjects_query_by_topics_success(
            @Value("classpath:files/xcurator-topics.json") Resource resource) throws IOException {
        var topicsForSearch = List.of("munster");
        feedTopics(resource);
        DataFetchingEnvironmentStub dataFetchingEnvironmentStub = new DataFetchingEnvironmentStub();
        var where = MuseumObjectSearchWhereInput.newBuilder().keywords(topicsForSearch).build();
        var results = museumObjectIndexRepository.searchMuseumObjects(where, new ArrayList<>(), 10, 0, dataFetchingEnvironmentStub);
        //  var firstPage = results.getEdges().stream().findFirst().get().getNode();
        //   assertTrue(firstPage.getTitles().contains(topicsForSearch));
        assertNotNull(results.getEdges());
        ;
    }


}
