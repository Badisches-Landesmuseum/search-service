package dreipc.asset.search.commands;

import dreipc.asset.search.models.PageTopics;
import dreipc.q8r.proto.asset.document.TopicDetectionProtos;
import org.springframework.stereotype.Component;

@Component
public class CreateTopicCommand implements Command {

    public PageTopics execute(TopicDetectionProtos.PageTopicsProto item) throws Exception {
        return PageTopics.builder().pageId(item.getPageId()).topics(item.getTopicsList()).build();
    }

    @Override
    public Operation getName() {
        return Operation.CREATE_TOPICS;
    }
}
