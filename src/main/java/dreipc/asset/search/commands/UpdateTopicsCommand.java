package dreipc.asset.search.commands;


import dreipc.asset.search.models.PageIndex;
import dreipc.asset.search.models.PageTopics;
import dreipc.asset.search.repositories.PageIndexRepository;
import dreipc.asset.search.utils.BatchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UpdateTopicsCommand implements Command {

    private final PageIndexRepository pageIndexRepository;

    public UpdateTopicsCommand(PageIndexRepository pageIndexRepository) {
        this.pageIndexRepository = pageIndexRepository;
    }

    public List<PageIndex> execute(List<? extends PageTopics> items) {
        var pageBatches = BatchUtil.batches(items.stream().collect(Collectors.toList()), 100);
        List<PageIndex> results = new ArrayList<>();
        pageBatches.forEach(batch -> {
            try {
                Map<String, PageTopics> itemsPerPage = batch.stream().distinct().collect(Collectors.toMap(PageTopics::getPageId, Function.identity()));
                var pages = pageIndexRepository.findAllByIdIn(itemsPerPage.keySet());
                pages.forEach(page -> page.setTopics(itemsPerPage.get(page.getId()).getTopics()));
                results.addAll(pages);
                log.info("Inserted topics to {} pages", pages.size());
            } catch (Exception e) {
                log.error("Topics were not saved", e);
            }
        });
        return results;
    }


    @Override
    public Operation getName() {
        return Operation.UPDATE_TOPICS;
    }
}
