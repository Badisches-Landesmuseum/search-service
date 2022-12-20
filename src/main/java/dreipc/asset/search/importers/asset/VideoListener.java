package dreipc.asset.search.importers.asset;


import dreipc.asset.search.commands.Command;
import dreipc.asset.search.commands.CreateAssetIndexCommand;
import dreipc.asset.search.commands.DeleteAssetIndexCommand;
import dreipc.asset.search.commands.Operation;
import dreipc.asset.search.models.AssetIndex;
import dreipc.asset.search.repositories.AssetIndexRepository;
import dreipc.asset.search.services.SyncPublisher;
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
public class VideoListener {
    private final AssetIndexRepository repository;
    private final SyncPublisher syncPublisher;
    private final Map<Operation, Command> commands;

    public VideoListener(AssetIndexRepository repository, SyncPublisher syncPublisher, List<Command> commands) {
        this.repository = repository;
        this.syncPublisher = syncPublisher;
        this.commands = commands.stream()
                .collect(Collectors.toMap(Command::getName, command -> command));
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.video.create", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "video.created"),
            containerFactory = "rabbitListenerContainerFactory")
    private void created(List<AssetProtos.VideoProto> list) {
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


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.video.sync", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "video.synced"),
            containerFactory = "rabbitListenerContainerFactory")
    private void synced(List<AssetProtos.VideoSyncProto> list) {
        var indexes = new ArrayList<AssetIndex>();
        list.forEach(proto -> {
            try {
                var asset = ((CreateAssetIndexCommand) commands.get(Operation.CREATE_ASSET_INDEX)).execute(proto.getVideo());
                indexes.add(asset);
                syncPublisher.publish(asset, proto.getSyncId());
            } catch (Exception e) {
                log.error("Batch of assets wasn't saved", e);
            }
        });
        repository.saveAll(indexes);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.video.delete", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "video.deleted"),
            containerFactory = "rabbitListenerContainerFactory")
    private void deleted(List<AssetProtos.AssetDeletedEvent> list) {
        var assetIds = list.stream().map(proto -> proto.getId()).toList();
        ((DeleteAssetIndexCommand) commands.get(Operation.DELETE_ASSET_INDEX)).execute(assetIds);
    }


}
