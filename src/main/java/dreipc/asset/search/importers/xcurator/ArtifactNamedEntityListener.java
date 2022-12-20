package dreipc.asset.search.importers.xcurator;

import dreipc.asset.search.utils.NamedEntityParser;
import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import dreipc.q8r.proto.asset.document.NamedEntitiesProtos;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactNamedEntityListener {

    private final NamedEntityParser namedEntityParser;
    private final MuseumObjectIndexRepository repository;


    public ArtifactNamedEntityListener(NamedEntityParser namedEntityParser, MuseumObjectIndexRepository repository) {
        this.namedEntityParser = namedEntityParser;
        this.repository = repository;
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.xcurator.entities.indexed", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "xcurator.entities.saved"),
            containerFactory = "rabbitListenerContainerFactory")
    public void addNamedEntities(List<NamedEntitiesProtos.NamedEntitiesSavedEventProto> protoList) {

        protoList.forEach(eventProto -> {
            repository.findById(eventProto.getId()).ifPresent(objectIndex -> {
                objectIndex.setNamedEntities(eventProto.getEntitiesList().stream().map(namedEntityProto -> namedEntityParser.parse(namedEntityProto)).toList());
                repository.save(objectIndex);
            });
        });
    }

}
