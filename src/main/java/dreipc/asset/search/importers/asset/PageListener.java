package dreipc.asset.search.importers.asset;

import dreipc.asset.search.commands.CreatPageIndexCommand;
import dreipc.asset.search.models.PageIndex;
import dreipc.asset.search.services.SyncPublisher;
import dreipc.asset.search.repositories.PageIndexRepository;
import dreipc.q8r.proto.asset.AssetProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.*;

@Slf4j
@Component
public class PageListener {

    private final CreatPageIndexCommand creatPageIndexCommand;
    private final PageIndexRepository repository;

    private final SyncPublisher syncPublisher;


    public PageListener(CreatPageIndexCommand creatPageIndexCommand, PageIndexRepository repository, SyncPublisher syncPublisher) {
        this.creatPageIndexCommand = creatPageIndexCommand;
        this.repository = repository;
        this.syncPublisher = syncPublisher;
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.page.create", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "page.created"),
            containerFactory = "rabbitListenerContainerFactory")
    private void created(List<AssetProtos.PageProto> list) {
        var indexes = new ArrayList<PageIndex>();
        list.forEach(pageProtoEvent -> {
            try {
                indexes.add(creatPageIndexCommand.execute(pageProtoEvent));
            } catch (Exception e) {
                log.error("Batch of pages wasn't saved", e);
            }
        });
        repository.saveAll(indexes);
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.page.sync", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "page.synced"),
            containerFactory = "rabbitListenerContainerFactory")
    private void synced(List<AssetProtos.PageSyncProto> list) {
        var pagesBySyncId = list
                .stream()
                .collect(groupingBy(entry -> entry.getSyncId(),
                        mapping(proto -> creatPageIndexCommand.execute(proto.getPage()), toList())));

        pagesBySyncId.entrySet().stream().forEach(batch -> {
            repository.saveAll(batch.getValue())
                    .forEach(page -> syncPublisher.publish(page, batch.getKey()));

            try {
            } catch (Exception e) {
                log.error("Batch of pages wasn't saved", e);
            }

        });
    }


}
