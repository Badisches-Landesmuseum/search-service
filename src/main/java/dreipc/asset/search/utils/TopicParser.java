package dreipc.asset.search.utils;

import dreipc.asset.search.models.TopicIndex;
import dreipc.xcurator.proto.XCuratorProtos;
import org.springframework.stereotype.Component;

@Component
public class TopicParser {

    public TopicIndex parse(XCuratorProtos.TopicProto topicProto) {
        return TopicIndex.builder()
                .id(topicProto.getId())
                .topics(topicProto.getLabelsList())
                .weight(topicProto.getWeight())
                .build();
    }


}
