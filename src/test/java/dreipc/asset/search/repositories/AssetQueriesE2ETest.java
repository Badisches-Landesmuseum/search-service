package dreipc.asset.search.repositories;

import de.dreipc.q8r.asset.page.similarities.types.*;
import dreipc.asset.search.graphql.FilterMiddlewareElasticsearch;
import dreipc.asset.search.testutil.ElasticSearchConfig;
import dreipc.asset.search.testutil.stubs.DataFetchingEnvironmentStub;
import dreipc.asset.search.testutil.stubs.ElasticsearchContainerExtension;
import dreipc.common.graphql.query.mongodb.SortMiddlewareMongoDB;
import dreipc.asset.search.testutil.EntityGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = ElasticSearchConfig.class)
@ExtendWith(ElasticsearchContainerExtension.class)
@SpringBootTest(classes = {SortMiddlewareMongoDB.class, PageIndexRepository.class, AssetIndexRepository.class, FilterMiddlewareElasticsearch.class})
class AssetQueriesE2ETest {

    @Autowired
    private AssetIndexRepository assetIndexRepository;
    @Autowired
    private PageIndexRepository pageIndexRepository;
    @Autowired
    private MuseumObjectIndexRepository museumObjectIndexRepository;

    @Test
    void init() {
        assertNotNull(assetIndexRepository);
        assertNotNull(pageIndexRepository);
        assertNotNull(museumObjectIndexRepository);
    }

    @Test
    void asset_partialWordSearch_success(@Value("classpath:files/document.txt") Resource resource) {
        var content = asString(resource);
        var searchString = "Bericht Koalitionsvertrag";
        var asset = EntityGenerator.assetIndex(content);

        assetIndexRepository.save(asset);
        DataFetchingEnvironmentStub dataFetchingEnvironmentStub = new DataFetchingEnvironmentStub();
        var stringFilter = TextSearchFilter.newBuilder().contains(searchString).build();
        var project = ProjectWhereUniqueInput.newBuilder().build();
        var where = AssetSearchWhereInput.newBuilder().content(stringFilter).project(project).build();
        var results = assetIndexRepository.searchAssets(where, new ArrayList<>(), 1, 0, dataFetchingEnvironmentStub);
        assertEquals(results.getEdges().stream().findFirst().get().getNode().getContent(), content);
    }

    @Test
    void asset_startsWith_fileName_match_success() {
        var searchString = "documen";
        var asset = EntityGenerator.assetIndex(null);
        asset.setFileName("document.txt");
        assetIndexRepository.save(asset);
        DataFetchingEnvironmentStub dataFetchingEnvironmentStub = new DataFetchingEnvironmentStub();

        var stringFilter = TextSearchFilter.newBuilder().startsWith(searchString).build();
        var project = ProjectWhereUniqueInput.newBuilder().build();
        var where = AssetSearchWhereInput.newBuilder().content(stringFilter).project(project).build();
        var results = assetIndexRepository.searchAssets(where, new ArrayList<>(), 1, 0, dataFetchingEnvironmentStub);
        var firstResult = results.getEdges().stream().findFirst().get();
        assertEquals(firstResult.getNode().getFileName(), asset.getFileName());
    }


    @Test
    void asset_pageTags_success() {
        var tags = List.of("AI", "3PC");
        var pageWithTags = EntityGenerator.pageIndex("");
        pageWithTags.setTags(tags);
        var pageWithoutTags = EntityGenerator.pageIndex("");
        pageIndexRepository.saveAll(List.of(pageWithTags, pageWithoutTags));
        DataFetchingEnvironmentStub dataFetchingEnvironmentStub = new DataFetchingEnvironmentStub();
        var project = ProjectWhereUniqueInput.newBuilder().build();
        var where = PageSearchWhereInput.newBuilder().content("AI").project(project).build();
        var results = pageIndexRepository.searchPages(where, new ArrayList<>(), 1, 0, dataFetchingEnvironmentStub);
        var firstPage = results.getEdges().stream().findFirst().get().getNode();
        assertTrue(results.getTotalCount() != pageIndexRepository.count());
        Assertions.assertTrue(firstPage.getTags().equals(tags));
    }

    @Test
    @Order(0)
    @Disabled
    void namedEntities_success() {
        var namedEntity = EntityGenerator.namedentityindex("Elisabeth II.");
        var page = EntityGenerator.pageIndex("");
        page.setNamedEntities(List.of(namedEntity));
        pageIndexRepository.save(page);
        DataFetchingEnvironmentStub dataFetchingEnvironmentStub = new DataFetchingEnvironmentStub();
        var where = PageSearchWhereInput.newBuilder().content("Elisabeth II.").build();
        var results = pageIndexRepository.searchPages(where, new ArrayList<>(), 1, 0, dataFetchingEnvironmentStub);
        var firstPage = results.getEdges().stream().findFirst().get().getNode();
        Assertions.assertTrue(firstPage.getNamedEntities().contains(namedEntity));
    }


    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
