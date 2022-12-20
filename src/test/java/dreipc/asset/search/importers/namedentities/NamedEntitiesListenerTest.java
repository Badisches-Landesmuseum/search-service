package dreipc.asset.search.importers.namedentities;

import de.dreipc.q8r.asset.page.similarities.types.AssetType;
import dreipc.asset.search.importers.asset.NamedEntitiesListener;
import dreipc.asset.search.models.AssetIndex;
import dreipc.asset.search.repositories.AssetIndexRepository;
import dreipc.asset.search.testutil.UnitTestConfiguration;
import dreipc.asset.search.testutil.stubs.TestProtoPublisher;
import dreipc.q8r.proto.asset.document.NamedEntitiesProtos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import({UnitTestConfiguration.class})
@Disabled
@EnableConfigurationProperties
class NamedEntitiesListenerTest {

    @Autowired
    TestProtoPublisher publisher;

    @Autowired
    NamedEntitiesListener namedEntitiesListener;

    @Autowired
    AssetIndexRepository assetIndexRepository;

    @BeforeEach
    void setUp() {
        assertNotNull(publisher);
        assertNotNull(namedEntitiesListener);
        assertNotNull(assetIndexRepository);
    }


    @Test
    void storeNamedEntities_success() {
        assetIndexRepository.save(AssetIndex.builder()
                .id("556686")
                .projectId("1002")
                .assetType(AssetType.HYPERTEXT)
                .content("content")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        namedEntitiesListener.add(List.of(generateResultEventProto()));
        assertThat(publisher.events().get("page.similarities.entities.saved")).hasSize(1);


    }

    private NamedEntitiesProtos.NamedEntitiesSavedEventProto generateResultEventProto() {
        return NamedEntitiesProtos.NamedEntitiesSavedEventProto.newBuilder()
                .setId("575758")
                .addEntities(generateEntity())
                .build();
    }

    private NamedEntitiesProtos.NamedEntityProto generateEntity() {

        return NamedEntitiesProtos.NamedEntityProto.newBuilder()
                .setSourceId("556686")
                .setLiteral("3pc")
                .setType("ORGANISATION")
                .setStartPosition(0)
                .setEndPosition(3)
                .build();
    }
}
