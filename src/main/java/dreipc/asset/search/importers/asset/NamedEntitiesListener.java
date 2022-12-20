package dreipc.asset.search.importers.asset;


import de.dreipc.rabbitmq.ProtoPublisher;
import dreipc.asset.search.repositories.AssetIndexRepository;
import dreipc.asset.search.utils.NamedEntityParser;
import dreipc.asset.search.repositories.PageIndexRepository;
import dreipc.q8r.proto.asset.document.NamedEntitiesProtos;
import dreipc.q8r.proto.document.PageSimilaritiesProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class NamedEntitiesListener {

    private final AssetIndexRepository assetIndexRepository;
    private final PageIndexRepository pageIndexRepository;
    private final NamedEntityParser namedEntityParser;
    private final ProtoPublisher protoPublisher;

    public NamedEntitiesListener(AssetIndexRepository assetIndexRepository, NamedEntityParser namedEntityParser, PageIndexRepository pageIndexRepository, ProtoPublisher protoPublisher) {
        this.assetIndexRepository = assetIndexRepository;
        this.namedEntityParser = namedEntityParser;
        this.pageIndexRepository = pageIndexRepository;
        this.protoPublisher = protoPublisher;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.entities.updated", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "entities.entity.updated"),
            containerFactory = "rabbitListenerContainerFactory")
    public void update(List<NamedEntitiesProtos.NamedEntityProto> protoList) {

        protoList.forEach(eventProto -> {
            assetIndexRepository.findById(eventProto.getId()).ifPresent(asset -> {
                asset.getNamedEntities().removeIf(namedEntityIndex -> namedEntityIndex.getId().equals(eventProto.getId()));
                asset.getNamedEntities().add(namedEntityParser.parse(eventProto));
                assetIndexRepository.save(asset);
                var importedEvent = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                        .setSourceId(asset.getId())
                        .build();
                protoPublisher.sendEvent("page.similarities.entities.updated", importedEvent);
            });

            pageIndexRepository.findById(eventProto.getId()).ifPresent(page -> {
                page.getNamedEntities().removeIf(namedEntityIndex -> namedEntityIndex.getId().equals(eventProto.getId()));
                page.getNamedEntities().add(namedEntityParser.parse(eventProto));
                pageIndexRepository.save(page);
                var importedEvent = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                        .setSourceId(page.getId())
                        .build();
                protoPublisher.sendEvent("page.similarities.entities.updated", importedEvent);

            });

        });
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.entities.indexed", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "entities.saved"),
            containerFactory = "rabbitListenerContainerFactory")
    public void add(List<NamedEntitiesProtos.NamedEntitiesSavedEventProto> protoList) {

        protoList.forEach(eventProto -> {
            assetIndexRepository.findById(eventProto.getId()).ifPresent(asset -> {
                asset.setNamedEntities(eventProto.getEntitiesList().stream().map(namedEntityProto -> namedEntityParser.parse(namedEntityProto)).toList());
                assetIndexRepository.save(asset);
                var importedEvent = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                        .setSourceId(asset.getId())
                        .build();
                protoPublisher.sendEvent("page.similarities.entities.saved", importedEvent);
            });

            pageIndexRepository.findById(eventProto.getId()).ifPresent(page -> {
                page.setNamedEntities(eventProto.getEntitiesList().stream().map(namedEntityProto -> namedEntityParser.parse(namedEntityProto)).toList());
                page.setTags(eventProto.getTagsList());
                pageIndexRepository.save(page);

                var importedEvent = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                        .setSourceId(page.getId())
                        .build();
                protoPublisher.sendEvent("page.similarities.entities.saved", importedEvent);

            });

        });
    }
}
