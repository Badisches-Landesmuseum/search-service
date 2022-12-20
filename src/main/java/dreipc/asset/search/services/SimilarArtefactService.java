package dreipc.asset.search.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import de.dreipc.q8r.asset.page.similarities.types.MuseumObject;
import dreipc.common.graphql.relay.CountConnection;
import dreipc.asset.search.queries.NativeQueryInterrogator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class SimilarArtefactService {

    private final NativeQueryInterrogator nativeQueryInterrogator;

    public SimilarArtefactService(NativeQueryInterrogator nativeQueryInterrogator) {
        this.nativeQueryInterrogator = nativeQueryInterrogator;
    }

    public CountConnection<MuseumObject> getSimilar(String imageId, String projectId, int first, int skip, DgsDataFetchingEnvironment environment) {
        List<MuseumObject> museumObjects = new ArrayList<>();
        var totalCount = 0;
        try {
            var similarQuery = SimilarArtefactQuery.query(imageId, projectId, first, skip);
            var indexName = SimilarArtefactQuery.indexName(projectId);
            var jsonNode = nativeQueryInterrogator.search(indexName, similarQuery);

            totalCount = jsonNode
                    .get("hits")
                    .get("total")
                    .get("value")
                    .asInt();

            var resultHits = jsonNode
                    .get("hits")
                    .get("hits");
            if (resultHits instanceof ArrayNode arrayListNode) {
                StreamSupport
                        .stream(arrayListNode.spliterator(), false)
                        .forEach(resultJson -> {
                            try {
                                var artefactId = resultJson
                                        .get("fields")
                                        .get("reference_id")
                                        .get(0)
                                        .asText("");
                                museumObjects.add(MuseumObject
                                                          .newBuilder()
                                                          .id(artefactId)
                                                          .build());
                            }catch (Exception e){
                                // empty
                            }
                        });
            }
        } catch (
                IOException e) {
            log.error("error get similar museumObjects", e);
        }

        if (museumObjects.isEmpty()) return CountConnection.empty();
        return new CountConnection<>(museumObjects, totalCount, environment);
    }
}
