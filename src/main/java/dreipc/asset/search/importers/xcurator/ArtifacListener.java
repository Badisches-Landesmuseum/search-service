package dreipc.asset.search.importers.xcurator;

import dreipc.asset.search.commands.Command;
import dreipc.asset.search.commands.DeleteMuseumObjectIndexCommand;
import dreipc.asset.search.commands.Operation;
import dreipc.asset.search.models.MuseumObjectIndex;
import dreipc.asset.search.services.SyncPublisher;
import dreipc.asset.search.commands.CreateMuseumObjectIndexCommand;
import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import dreipc.xcurator.proto.XCuratorProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Slf4j
public class ArtifacListener {

    private final MuseumObjectIndexRepository repository;
    private final SyncPublisher syncPublisher;

    private final Map<Operation, Command> commands;

    public ArtifacListener(MuseumObjectIndexRepository repository, SyncPublisher syncPublisher, List<Command> commands) {
        this.repository = repository;
        this.syncPublisher = syncPublisher;
        this.commands = commands.stream()
                .collect(Collectors.toMap(Command::getName, command -> command));
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.museumobject.create", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "xcurator.museumobject.created"),
            containerFactory = "rabbitListenerContainerFactory")
    private void created(List<XCuratorProtos.MuseumObjectProto> list) {
        var indexes = new ArrayList<MuseumObjectIndex>();
        list.forEach(proto -> {
            try {
                var created = ((CreateMuseumObjectIndexCommand) commands.get(Operation.CREATE_MUSEUM_OBJECT_INDEX)).execute(proto);
                indexes.add(created);
            } catch (Exception e) {
                log.error("Batch of assets wasn't saved", e);
            }
        });
        repository.saveAll(indexes);
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.museumobject.sync", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "xcurator.museumobject.synced"),
            containerFactory = "rabbitListenerContainerFactory")
    private void synced(List<XCuratorProtos.MuseumObjectSyncProto> list) {
        var indexes = new ArrayList<MuseumObjectIndex>();
        list.forEach(proto -> {
            try {
                var created = ((CreateMuseumObjectIndexCommand) commands.get(Operation.CREATE_MUSEUM_OBJECT_INDEX)).execute(proto.getMuseumObject());
                indexes.add(created);
                syncPublisher.publish(created, proto.getSyncId());
            } catch (Exception e) {
                log.error("Batch of assets wasn't saved", e);
            }
        });
        repository.saveAll(indexes);
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.museumobject.delete", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "xcurator.museumobject.deleted"),
            containerFactory = "rabbitListenerContainerFactory")
    private void deleted(List<XCuratorProtos.MuseumObjectDeletedEvent> list) {
        var deletedIds = list.stream().map(proto -> proto.getId()).toList();
        ((DeleteMuseumObjectIndexCommand) commands.get(Operation.DELETE_MUSEUM_OBJECT_INDEX)).execute(deletedIds);
    }


}
