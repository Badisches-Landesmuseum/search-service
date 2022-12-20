package dreipc.asset.search.queries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DistributionQueryBuilder {

    public static String query(int clusterSize, String fieldName) {
        return QUERY.replace("<CLUSTER_SIZE>", String.valueOf(clusterSize))
                .replace("<FIELD_NAME>", fieldName + ".keyword");
    }

    private final static String QUERY = """   
              "aggs": {
                "top_clusters": {
                  "terms": {
                    "field": "<FIELD_NAME>",
                    "size": <CLUSTER_SIZE>
                  }
                }
              }
            """;


}
