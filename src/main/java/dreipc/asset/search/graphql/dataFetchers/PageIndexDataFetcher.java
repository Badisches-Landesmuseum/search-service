package dreipc.asset.search.graphql.dataFetchers;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import de.dreipc.q8r.asset.page.similarities.types.Page;
import de.dreipc.q8r.asset.page.similarities.types.PageSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.PageSearchWhereInput;
import de.dreipc.q8r.asset.page.similarities.types.PageUniqueInput;
import dreipc.common.graphql.relay.CountConnection;
import dreipc.asset.search.repositories.PageIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@DgsComponent
public class PageIndexDataFetcher {

    private final PageIndexRepository repository;

    public PageIndexDataFetcher(PageIndexRepository repository) {
        this.repository = repository;
    }

    @DgsQuery
    public CompletableFuture<CountConnection<Page>> searchPages(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument PageSearchWhereInput where,
            @InputArgument List<PageSearchOrderByInput> orderBy,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var indexes = repository.searchPages(where, orderBy, first, skip, environment);
            var searchResults = indexes.getEdges().stream().map(indexEdge ->
                            indexEdge.getNode().toSource())
                    .toList();
            return new CountConnection<>(searchResults, searchResults.size(), environment);
        });
    }


    @DgsQuery
    public CompletableFuture<CountConnection<Page>> searchSimilarPages(
            @InputArgument Integer first,

            @InputArgument Integer skip,
            @InputArgument PageUniqueInput where,
            @InputArgument List<PageSearchOrderByInput> orderBy,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var indexes = repository.searchSimilarPages(where, orderBy, first, skip, environment);
            var searchResults = indexes.getEdges().stream().map(indexEdge ->
                            indexEdge.getNode().toSource())
                    .toList();
            return new CountConnection<>(searchResults, searchResults.size(), environment);
        });

    }

    @DgsQuery
    public Set<String> similarTags(
            @InputArgument PageUniqueInput where,
            @InputArgument Integer first,
            @InputArgument Integer skip,
            DgsDataFetchingEnvironment environment
    ) {

        assert environment != null;

        var similarPages = repository.searchSimilarPages(where, null, first, skip, environment);

        var tags = new HashSet<String>();

        similarPages.getEdges().forEach(item -> {
            item.getNode().getPageTags().forEach(tag -> {
                if (tags.size() > first) return;
                tags.add(tag);
            });
        });
        return tags;
    }


}
