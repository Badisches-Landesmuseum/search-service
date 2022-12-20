package dreipc.asset.search.commands;


import dreipc.asset.search.repositories.AssetIndexRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteAssetIndexCommand implements Command {

    private final AssetIndexRepository repository;

    public DeleteAssetIndexCommand(AssetIndexRepository repository) {
        this.repository = repository;
    }

    public void execute(String assetId) {
        repository.deleteById(assetId);
    }

    public void execute(List<String> assetIds) {
        repository.deleteAllById(assetIds);
    }

    @Override
    public Operation getName() {
        return Operation.DELETE_ASSET_INDEX;
    }
}
