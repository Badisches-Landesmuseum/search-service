package dreipc.asset.search.importers.xcurator;

import dreipc.asset.search.services.xCuratorCleanService;
import dreipc.q8r.proto.asset.AssetProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ImportInitListener {

    private final xCuratorCleanService cleanMutationResolver;

    public ImportInitListener(xCuratorCleanService cleanMutationResolver) {
        this.cleanMutationResolver = cleanMutationResolver;
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.import.init", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "asset.asset.import.init"))
    void cleanUpBeforeInit(AssetProtos.AssetStoreInitActionProto actionProto) {
        var projectId = actionProto.getProjectId();
        try {
            cleanMutationResolver.clean(projectId);
            log.info("Cleaned Project ("+projectId+") for initialization:");
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(
                    "Unable to clean up project (" + projectId + ") for initialization.");
        }
    }
}
