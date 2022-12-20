package dreipc.asset.search.commands;


import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteMuseumObjectIndexCommand implements Command {

    private final MuseumObjectIndexRepository repository;

    public DeleteMuseumObjectIndexCommand(MuseumObjectIndexRepository repository) {
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
        return Operation.DELETE_MUSEUM_OBJECT_INDEX;
    }
}
