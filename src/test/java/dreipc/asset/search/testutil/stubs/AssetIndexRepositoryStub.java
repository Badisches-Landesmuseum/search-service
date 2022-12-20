package dreipc.asset.search.testutil.stubs;

import de.dreipc.q8r.asset.page.similarities.types.*;
import dreipc.asset.search.models.AssetIndex;
import dreipc.asset.search.models.PanquraArticleIndexSearchResult;
import dreipc.asset.search.repositories.AssetIndexRepository;
import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AssetIndexRepositoryStub implements AssetIndexRepository {


    @Override
    public Page<AssetIndex> searchSimilar(AssetIndex entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<AssetIndex> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<AssetIndex> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AssetIndex> S save(S entity) {
        return null;
    }

    @Override
    public <S extends AssetIndex> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AssetIndex> findById(String s) {

        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<AssetIndex> findAll() {
        return null;
    }

    @Override
    public Iterable<AssetIndex> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(AssetIndex entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends AssetIndex> entities) {

    }

    @Override
    public void deleteAll() {

    }


    @Override
    public CountConnection<AssetIndex> searchAssets(AssetSearchWhereInput where, List<AssetSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return new CountConnection<>(Collections.emptyList(), 0, null);
    }

    @Override
    public CountConnection<PanquraArticleIndexSearchResult> searchWithHighlights(PanquraArticleSearchWhereInput where, List<PanquraArticleOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return new CountConnection<>(Collections.emptyList(), 0, null);
    }
}
