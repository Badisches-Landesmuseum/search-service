package dreipc.asset.search.testutil.stubs;

import de.dreipc.q8r.asset.page.similarities.types.PageSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.PageSearchWhereInput;
import de.dreipc.q8r.asset.page.similarities.types.PageUniqueInput;
import dreipc.asset.search.models.PageIndex;
import dreipc.asset.search.repositories.PageIndexRepository;
import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PageIndexRepositoryStub implements PageIndexRepository {


    @Override
    public List<PageIndex> findAllByIdIn(Collection<String> ids) {
        return null;
    }

    @Override
    public void deleteAllByAssetId(String id) {

    }

    @Override
    public Page<PageIndex> searchSimilar(PageIndex entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<PageIndex> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PageIndex> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PageIndex> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PageIndex> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PageIndex> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<PageIndex> findAll() {
        return null;
    }

    @Override
    public Iterable<PageIndex> findAllById(Iterable<String> strings) {
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
    public void delete(PageIndex entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends PageIndex> entities) {

    }

    @Override
    public void deleteAll() {

    }


    @Override
    public CountConnection<PageIndex> searchPages(PageSearchWhereInput where, List<PageSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return new CountConnection<>(Collections.emptyList(), 0, null);
    }

    @Override
    public CountConnection<PageIndex> searchSimilarPages(PageUniqueInput where, List<PageSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return new CountConnection<>(Collections.emptyList(), 0, null);
    }

}
