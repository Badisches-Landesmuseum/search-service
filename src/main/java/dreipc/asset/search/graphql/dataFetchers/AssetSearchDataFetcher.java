package dreipc.asset.search.graphql.dataFetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import de.dreipc.q8r.asset.page.similarities.types.*;
import dreipc.common.graphql.relay.CountConnection;
import dreipc.asset.search.repositories.AssetIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.UUIDs;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j

@DgsComponent
public class AssetSearchDataFetcher {

    private final AssetIndexRepository repository;

    public AssetSearchDataFetcher(AssetIndexRepository repository) {
        this.repository = repository;
    }


    @DgsQuery
    public CompletableFuture<CountConnection<Asset>> searchAssets(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument AssetSearchWhereInput where,
            @InputArgument List<AssetSearchOrderByInput> orderBy,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var indexes = repository.searchAssets(where, orderBy, first, skip, environment);
            var searchResults = indexes.getEdges().stream().map(assetIndex ->
                            assetIndex.getNode().toSource())
                    .toList();
            return new CountConnection<>(searchResults, indexes.getTotalCount(), environment);
        });
    }

    @DgsQuery
    public CompletableFuture<CountConnection<PanquraArticleSearchResult>> panquraArticles(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument PanquraArticleSearchWhereInput where,
            @InputArgument List<PanquraArticleOrderByInput> orderBy,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var indexes = repository.searchWithHighlights(where, orderBy, first, skip, environment);
            var searchResults = indexes.getEdges().stream().map(assetIndex ->
                            PanquraArticleSearchResult.newBuilder()
                                    .id(UUIDs.randomBase64UUID())
                                    .hypertext((Hypertext) assetIndex.getNode().getIndex().toSource())
                                    .highlights(assetIndex.getNode().getHighlights())
                                    .build())
                    .toList();
            return new CountConnection<>(searchResults, indexes.getTotalCount(), environment);
        });
    }


}
