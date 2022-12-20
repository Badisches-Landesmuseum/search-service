package dreipc.asset.search.queries;

import org.springframework.stereotype.Service;

@Service
public class DeleteQuery {

    private final NativeQueryInterrogator elasticTemplate;
    private final String ASSETS_BY_PROJECT = """
            {
              "query": {
                "match": {
                  "projectId": "<PROJECT_ID>"
                }
              }
            }
            """;

    private final String MATCH_ALL = """
            {
              "query": {
                "match_all": {}
              }
            }
            """;

    public DeleteQuery(NativeQueryInterrogator elasticTemplate) {
        this.elasticTemplate = elasticTemplate;
    }

    public boolean assetsByProjectId(String projectId){
        var query = ASSETS_BY_PROJECT.replace("<PROJECT_ID>", projectId);
        try {
            elasticTemplate.delete( "asset", query);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean cleanIndex(String indexName){
        try {
            elasticTemplate.delete(indexName, MATCH_ALL);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
