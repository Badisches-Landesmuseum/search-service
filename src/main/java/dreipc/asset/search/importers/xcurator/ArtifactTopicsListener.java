package dreipc.asset.search.importers.xcurator;

import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import dreipc.asset.search.utils.TopicParser;
import dreipc.xcurator.proto.XCuratorProtos;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactTopicsListener {

    private final TopicParser topicParser;
    private final MuseumObjectIndexRepository repository;

    public ArtifactTopicsListener(TopicParser topicParser, MuseumObjectIndexRepository repository) {
        this.topicParser = topicParser;
        this.repository = repository;
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.xcurator.topics.indexed", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "xcurator.topics.saved"),
            containerFactory = "rabbitListenerContainerFactory")
    public void addTopics(List<XCuratorProtos.TopicProto> protoList) {
        protoList.forEach(eventProto -> {
            repository.findById(eventProto.getSourceId()).ifPresent(objectIndex -> {
                objectIndex.getTopics().add(topicParser.parse(eventProto));
                repository.save(objectIndex);
            });
        });
    }
}
