package dreipc.asset.search.graphql.dataFetchers;

import com.netflix.graphql.dgs.*;
import de.dreipc.q8r.asset.page.similarities.types.*;
import dreipc.asset.search.models.EpochGroup;
import dreipc.asset.search.models.MuseumObjectCountConnection;
import dreipc.asset.search.services.*;
import dreipc.common.graphql.relay.CountConnection;
import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import graphql.relay.Edge;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j

@DgsComponent
public class MuseumObjectSearchDataFetcher {

    private final MuseumObjectIndexRepository repository;
    private final ColorSearchService colorSearchService;
    private final EpochDistributionService epochDistributionService;

    private final CountryDistributionService countryDistributionService;

    private final MaterialDistributionService materialDistributionService;

    private final SimilarArtefactService similarArtefactService;

    public MuseumObjectSearchDataFetcher(MuseumObjectIndexRepository repository, ColorSearchService colorSearchService, EpochDistributionService epochDistributionService, CountryDistributionService countryDistributionService, MaterialDistributionService materialDistributionService, SimilarArtefactService similarArtefactService) {
        this.repository = repository;
        this.colorSearchService = colorSearchService;
        this.epochDistributionService = epochDistributionService;
        this.countryDistributionService = countryDistributionService;
        this.materialDistributionService = materialDistributionService;
        this.similarArtefactService = similarArtefactService;
    }


    @DgsQuery
    public CompletableFuture<CountConnection<MuseumObject>> museumObjects(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument MuseumObjectSearchWhereInput where,
            @InputArgument List<MuseumObjectSearchOrderByInput> orderBy,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var indexes = repository.searchMuseumObjects(where, orderBy, first, skip, environment);
            var searchResults = indexes.getEdges().stream().map(indexEdge ->
                            indexEdge.getNode().toSource())
                    .toList();
            return new MuseumObjectCountConnection<>(searchResults, indexes.getTotalCount(), environment, indexes.getKeywords());
        });
    }


    @DgsQuery
    public CompletableFuture<CountConnection<ExceptionalMuseumObject>> exceptionalMuseumObjects(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument MuseumObjectSearchWhereInput where,
            @InputArgument List<MuseumObjectSearchOrderByInput> orderBy,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var indexes = repository.searchMuseumObjects(where, orderBy, 1500, 0, environment);
            var resultIds = indexes.getEdges().stream().map(indexEdge ->
                            indexEdge.getNode().getId())
                    .toList();

            var materialsGroups = materialDistributionService.getLeastDistributions(resultIds);
            where.setMaterials(materialsGroups.stream().map(MaterialGroup::getName).toList());
            indexes = repository.searchMuseumObjects(where, orderBy, first, skip, environment);
            var searchResults = indexes.getEdges().stream().map(indexEdge ->
                            ExceptionalMuseumObject.newBuilder().museumObject(indexEdge.getNode().toSource())
                                    .reason(List.of(ExceptionalReason.MATERIAL)).build()
                    )
                    .toList();
            return new MuseumObjectCountConnection(searchResults, indexes.getTotalCount(), environment, indexes.getKeywords());
        });
    }


    @DgsQuery
    public CompletableFuture<CountConnection<MuseumObject>> similarMuseumObjects(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument ImageMuseumObjectSearchWhereInput where,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var objectsRelay = similarArtefactService.getSimilar(where.getImageId(), where.getProjectId(), first, skip, environment);
            var objects = objectsRelay.getEdges().stream().map(Edge::getNode).toList();

            return new MuseumObjectCountConnection<>(objects, objectsRelay.getTotalCount(), environment);
        });
    }


    @DgsQuery
    private CompletableFuture<CountConnection<MuseumObjectColorSearchResult>> museumObjectsByColor(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            @InputArgument MuseumObjectColorSearchWhereInput where,
            DgsDataFetchingEnvironment environment
    ) {
        return CompletableFuture.supplyAsync(() -> {
            var hexColor = where.getColor();
            return colorSearchService.artefactsByColor(hexColor, first, skip, environment);
        });
    }

    @DgsQuery
    public CompletableFuture<List<String>> smartKeywords(
            @InputArgument Integer first,
            @InputArgument Integer skip,
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            var keywords = repository.smartKeywords(first, skip);
            return keywords;
        });
    }

    @DgsQuery
    public CompletableFuture<List<MaterialGroup>> materialDistribution(
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            return materialDistributionService.getDistributions(null);
        });
    }


    @DgsQuery
    public CompletableFuture<List<CountryGroup>> countryDistribution(
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            return countryDistributionService.getDistributions();
        });
    }


    @DgsQuery
    public CompletableFuture<List<dreipc.asset.search.models.EpochGroup>> epochDistribution(
            DgsDataFetchingEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> {
            return epochDistributionService.getDistributions();
        });
    }


    @DgsData(parentType = "EpochGroup")
    public CompletableFuture<Integer> start(@NotNull DgsDataFetchingEnvironment dfe) {
        var epochView = (dreipc.asset.search.models.EpochGroup) dfe.getSource();
        var epoch = EpochDistributionService.byName(epochView.name()).orElseThrow(() -> new IllegalArgumentException("Epoch with name (" + epochView.name() + " ) is unknown."));
        return CompletableFuture.supplyAsync(epoch::start);
    }

    @DgsData(parentType = "EpochGroup")
    public CompletableFuture<Integer> end(@NotNull DgsDataFetchingEnvironment dfe) {
        var epochView = (EpochGroup) dfe.getSource();
        var epoch = EpochDistributionService.byName(epochView.name()).orElseThrow(() -> new IllegalArgumentException("Epoch with name (" + epochView.name() + " ) is unknown."));
        return CompletableFuture.supplyAsync(() -> {
            var end = epoch.end();
            if (end.equals(Integer.MAX_VALUE))
                end = Calendar.getInstance().get(Calendar.YEAR);
            return end;
        });
    }

}
