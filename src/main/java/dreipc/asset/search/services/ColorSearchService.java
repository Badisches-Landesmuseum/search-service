package dreipc.asset.search.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import de.dreipc.q8r.asset.page.similarities.types.Image;
import de.dreipc.q8r.asset.page.similarities.types.MuseumObject;
import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectColorSearchResult;
import dreipc.common.graphql.relay.CountConnection;
import dreipc.asset.search.queries.NativeQueryInterrogator;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ColorSearchService {

    private static final String COLOR_INDEX = "colors";
    private final NativeQueryInterrogator nativeQueryInterrogator;

    public ColorSearchService(NativeQueryInterrogator nativeQueryInterrogator) {
        this.nativeQueryInterrogator = nativeQueryInterrogator;
    }

    public CountConnection<MuseumObjectColorSearchResult> artefactsByColor(String hexColor, int first, int skip, DataFetchingEnvironment environment) {

        List<MuseumObjectColorSearchResult> museumObjects = new ArrayList<>();
        var totalCount = 0;
        try {
            var stringQuery =  ColorSearchQuery.query(hexColor, first, skip);
            var jsonNode = nativeQueryInterrogator.search(COLOR_INDEX, stringQuery);

            totalCount = jsonNode
                    .get("hits")
                    .get("total")
                    .get("value")
                    .asInt();

            var resultHits = jsonNode
                    .get("hits")
                    .get("hits");

            if(resultHits instanceof ArrayNode results){
                StreamSupport.stream(results.spliterator(), false).forEach(result -> {
                    var id = result.get("_source").get("id").asText();
                    var artefactId = result.get("_source").get("artefactId").asText();
                    var object = MuseumObjectColorSearchResult.newBuilder()
                            .museumObject(MuseumObject.newBuilder().id(artefactId).build())
                            .image(Image.newBuilder().id(id).build())
                                                 .build();
                    museumObjects.add(object);
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (museumObjects.isEmpty())
            return CountConnection.empty();

        return new CountConnection<>(museumObjects, totalCount, environment);
    }
}
