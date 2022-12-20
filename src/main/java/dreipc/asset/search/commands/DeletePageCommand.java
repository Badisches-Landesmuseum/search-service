package dreipc.asset.search.commands;


import de.dreipc.rabbitmq.ProtoPublisher;
import dreipc.asset.search.repositories.PageIndexRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeletePageCommand implements Command {

    private final PageIndexRepository pageIndexRepository;
    private final ProtoPublisher protoPublisher;

    public DeletePageCommand(PageIndexRepository pageIndexRepository, ProtoPublisher protoPublisher) {
        this.pageIndexRepository = pageIndexRepository;
        this.protoPublisher = protoPublisher;

    }

    public void executeByAssetIds(List<String> assetIds) {
        assetIds.forEach(assetId -> {
            pageIndexRepository.deleteAllByAssetId(assetId);
        });
    }

    @Override
    public Operation getName() {
        return Operation.DELETE_PAGE_INDEX;
    }
}
