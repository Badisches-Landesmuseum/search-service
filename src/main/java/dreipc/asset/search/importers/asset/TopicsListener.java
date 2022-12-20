package dreipc.asset.search.importers.asset;


import dreipc.asset.search.commands.CreateTopicCommand;
import dreipc.asset.search.commands.UpdateTopicsCommand;
import dreipc.asset.search.models.PageTopics;
import dreipc.asset.search.repositories.PageIndexRepository;
import dreipc.q8r.proto.asset.document.TopicDetectionProtos;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TopicsListener {

    private final CreateTopicCommand createTopicCommand;
    private final UpdateTopicsCommand updateTopicsCommand;
    private final PageIndexRepository pageIndexRepository;

    public TopicsListener(CreateTopicCommand createTopicCommand, UpdateTopicsCommand updateTopicsCommand, PageIndexRepository pageIndexRepository) {

        this.createTopicCommand = createTopicCommand;
        this.updateTopicsCommand = updateTopicsCommand;
        this.pageIndexRepository = pageIndexRepository;
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.topics.created", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "page.similarities.topic.detected"),
            containerFactory = "rabbitListenerContainerFactory")
    private void addDocumentAction(List<TopicDetectionProtos.TopicDetectionProto> createdProto) throws Exception {
        List<PageTopics> topics = new ArrayList<>();

        createdProto.forEach(proto -> {
            proto.getPageTopicsList().forEach(item -> {
                try {
                    topics.add(createTopicCommand.execute(item));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        pageIndexRepository.saveAll(updateTopicsCommand.execute(topics));

    }


}
