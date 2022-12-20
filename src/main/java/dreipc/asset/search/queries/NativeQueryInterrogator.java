package dreipc.asset.search.queries;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
public class NativeQueryInterrogator {

    private final RestClient restClient;

    private final ObjectMapper jsonMapper;
    private TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
    };

    public NativeQueryInterrogator(RestHighLevelClient highLevelClient, ObjectMapper jsonMapper) {
        this.restClient = highLevelClient.getLowLevelClient();
        this.jsonMapper = jsonMapper;
    }

    public JsonNode search(String indexName, NativeQueryBuilder nativeQueryBuilder) throws IOException {
        return search(indexName, nativeQueryBuilder.getQueryBody());
    }

    public JsonNode search(String indexName, String query) throws IOException {
        Request scriptRequest = new Request("POST", "/" + indexName + "/_search");
        scriptRequest.setJsonEntity(query);
        var response = restClient.performRequest(scriptRequest);
        var bodyString = getResponseBodyString(response);
        return jsonMapper.readTree(bodyString);
    }

    public JsonNode delete(String indexName, String query) throws IOException {
        Request scriptRequest = new Request("POST", "/" + indexName + "/_delete_by_query");
        scriptRequest.setJsonEntity(query);
        var response = restClient.performRequest(scriptRequest);
        var bodyString = getResponseBodyString(response);
        return jsonMapper.readTree(bodyString);
    }


    private String getResponseBodyString(Response response) {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }


}
