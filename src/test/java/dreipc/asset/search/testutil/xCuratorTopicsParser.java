package dreipc.asset.search.testutil;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dreipc.asset.search.models.TopicIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@TestComponent
@Slf4j
public class xCuratorTopicsParser {



    public Map<String, List<TopicIndex>> parseFromJson(Resource resource) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var jsonNode = objectMapper.readTree(resource.getFile());
        if (jsonNode instanceof ArrayNode arrayListNode) {
            var topics = groupBySourceId(arrayListNode);
            if (!topics.isEmpty()) return topics;
        }
        throw new NullPointerException("Topics are empty, are you missing topics json file?");

    }

    public Map<String, List<TopicIndex>> groupBySourceId(ArrayNode arrayNode) {
        HashMap<String, List<TopicIndex>> groupedBySourceId = new HashMap<>();

        arrayNode.forEach(jsonNode ->
                {
                    var sourceId = getObjectId(jsonNode, "sourceId");
                    var array = groupedBySourceId.getOrDefault(sourceId, new ArrayList<>());
                    array.add(parse(jsonNode));
                    if (!array.isEmpty()) groupedBySourceId.put(sourceId, array);
                }
        );
        return groupedBySourceId;
    }

    public String getObjectId(JsonNode jsonNode, String fieldName) {

        return jsonNode.get(fieldName).get("$oid").asText();
    }

    public TopicIndex parse(JsonNode jsonNode) {
        try {
            var topicIndex = TopicIndex.builder();

            var id = getObjectId(jsonNode, "_id");
            topicIndex.id(id);

            var listNode = jsonNode.get("labels");
            if (listNode instanceof ArrayNode arrayListNode) {
                topicIndex.topics(getListOrDefault(arrayListNode));
            }

            topicIndex.weight((float) jsonNode.get("weight").doubleValue());

            return topicIndex.build();
        } catch (Exception e) {
            log.info("cant parse object", e);
            return null;
        }


    }

    public List<String> getListOrDefault(ArrayNode arrayListNode) {
        List<String> defaultValue = new ArrayList<>();
        StreamSupport.stream(arrayListNode.spliterator(), false).forEach(jsonNode -> defaultValue.add(jsonNode.asText()));
        return defaultValue;
    }


}
