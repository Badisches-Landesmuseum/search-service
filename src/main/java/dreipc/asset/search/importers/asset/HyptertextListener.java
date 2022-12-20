package dreipc.asset.search.importers.asset;

import dreipc.asset.search.commands.*;
import dreipc.asset.search.repositories.AssetIndexRepository;
import dreipc.asset.search.services.SyncPublisher;
import dreipc.asset.search.models.AssetIndex;
import dreipc.q8r.proto.asset.AssetProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class HyptertextListener {

    private final AssetIndexRepository repository;
    private final SyncPublisher syncPublisher;
    private final Map<Operation, Command> commands;

    public HyptertextListener(AssetIndexRepository repository, SyncPublisher syncPublisher, List<Command> commands) {
        this.repository = repository;
        this.syncPublisher = syncPublisher;
        this.commands = commands.stream()
                .collect(Collectors.toMap(Command::getName, command -> command));
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.hypertext.create", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "hypertext.created"),
            containerFactory = "rabbitListenerContainerFactory")
    private void created(List<AssetProtos.HypertextProto> list) {
        var indexes = new ArrayList<AssetIndex>();
        list.forEach(proto -> {
            try {
                var asset = ((CreateAssetIndexCommand) commands.get(Operation.CREATE_ASSET_INDEX)).execute(proto);
                indexes.add(asset);
            } catch (Exception e) {
                log.error("Batch of assets wasn't saved", e);
            }
        });
        repository.saveAll(indexes);
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.hypertext.sync", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "hypertext.synced"),
            containerFactory = "rabbitListenerContainerFactory")
    private void synced(List<AssetProtos.HypertextSyncProto> list) {
        var indexes = new ArrayList<AssetIndex>();
        list.forEach(proto -> {
            try {
                var asset = ((CreateAssetIndexCommand) commands.get(Operation.CREATE_ASSET_INDEX)).execute(proto.getHypertext());
                indexes.add(asset);
                syncPublisher.publish(asset, proto.getSyncId());
            } catch (Exception e) {
                log.error("Batch of assets wasn't saved", e);
            }
        });
        repository.saveAll(indexes);
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.hypertext.delete", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "hypertext.deleted"),
            containerFactory = "rabbitListenerContainerFactory")
    private void deleted(List<AssetProtos.AssetDeletedEvent> list) {
        var assetIds = list.stream().map(proto -> proto.getId()).toList();
        ((DeleteAssetIndexCommand) commands.get(Operation.DELETE_ASSET_INDEX)).execute(assetIds);
        ((DeletePageCommand) commands.get(Operation.DELETE_PAGE_INDEX)).executeByAssetIds(assetIds);
    }

}
