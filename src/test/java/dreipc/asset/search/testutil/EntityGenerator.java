package dreipc.asset.search.testutil;

import de.dreipc.q8r.asset.page.similarities.types.AssetType;
import dreipc.asset.search.models.*;
import org.elasticsearch.common.UUIDs;

import java.time.Instant;
import java.util.List;

public class EntityGenerator {


    public static PageIndex pageIndex(String content) {

        return PageIndex.builder().id(UUIDs.randomBase64UUID()).projectId("1002").assetId("348884").content(content).updatedAt(Instant.now()).build();
    }

    public static AssetIndex assetIndex(String content) {
        return AssetIndex.builder().id(UUIDs.randomBase64UUID()).projectId("1002").assetType(AssetType.HYPERTEXT).content(content).createdAt(Instant.now()).updatedAt(Instant.now()).build();
    }


    public static MuseumObjectIndex museumObjectIndex(List<String> keywords) {
        var museumIndex = EntityGenerator.museumObjectIndex();
        museumIndex.setKeywords(keywords);
        return museumIndex;
    }


    public static MuseumObjectIndex museumObjectIndexWithTopics(List<TopicIndex>topicIndices) {
        return MuseumObjectIndex.builder()
                .id(UUIDs.randomBase64UUID())
                .topics(topicIndices)
                .build();
    }

    public static MuseumObjectIndex museumObjectIndex() {
        return MuseumObjectIndex.builder()
                .id(UUIDs.randomBase64UUID())
                .build();
    }


    public static NamedEntityIndex namedentityindex(String literal) {
        return NamedEntityIndex.builder()
                .id(UUIDs.randomBase64UUID())
                .literal(literal)
                .type("PER")
                .build();
    }

}
