package dreipc.asset.search.testutil;


import de.dreipc.rabbitmq.ProtoPublisher;
import dreipc.asset.search.repositories.ColorIndexRepository;
import dreipc.asset.search.testutil.stubs.*;
import dreipc.asset.search.repositories.AssetIndexRepository;
import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import dreipc.asset.search.repositories.PageIndexRepository;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@TestConfiguration
@MockBean(classes = {Exchange.class, RabbitTemplate.class, Exchange.class, ElasticsearchOperations.class, ElasticsearchRestTemplate.class})
@EnableAutoConfiguration(exclude = {ElasticsearchDataAutoConfiguration.class, ElasticsearchRepositoriesAutoConfiguration.class})
public class UnitTestConfiguration {

    @Bean
    @Primary
    public PageIndexRepository pageIndexRepository() {
        return new PageIndexRepositoryStub();
    }

    @Bean
    @Primary
    public AssetIndexRepository assetIndexRepository() {
        return new AssetIndexRepositoryStub();
    }

    @Bean
    @Primary
    public MuseumObjectIndexRepository museumObjectIndexRepository() {
        return new MuseumObjectIndexRepositoryStub();
    }

    @Bean
    @Primary
    ProtoPublisher publisherStub() {
        return new TestProtoPublisher();
    }

    @Bean
    @Primary
    public ColorIndexRepository colorIndexRepository() {
        return new ColorIndexRepositoryStub();
    }


    @Bean
    @Primary
    ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchOperationsStub();
    }

}
