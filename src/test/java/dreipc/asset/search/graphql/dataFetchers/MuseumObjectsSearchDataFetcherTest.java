package dreipc.asset.search.graphql.dataFetchers;

import dreipc.asset.search.testutil.GraphQLTestExecutor;
import dreipc.asset.search.testutil.UnitTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({UnitTestConfiguration.class})
@Slf4j
class MuseumObjectsSearchDataFetcherTest {


    @Autowired
    private TestRestTemplate restTemplate;

    private GraphQLTestExecutor graphqlExecutor;


    @BeforeEach
    void setUp() {
        this.graphqlExecutor = GraphQLTestExecutor.create(restTemplate);
    }

    @Test
    void testMuseumObjects(@Value("classpath:graphql/museumObjects.graphql") Resource graphqlQuery) throws IOException {
        var content = StreamUtils.copyToString(graphqlQuery.getInputStream(), Charset.defaultCharset());
        Map<String, Object> variables = new LinkedHashMap<>();
        var response = this.graphqlExecutor.request(content, variables);
        var data = response.get("data");
        var errors = response.get("errors");
        assertNull(errors, "museumObjects query returned errors");
    }


}
