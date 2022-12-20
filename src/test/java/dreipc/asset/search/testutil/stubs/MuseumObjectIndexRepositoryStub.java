package dreipc.asset.search.testutil.stubs;

import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchWhereInput;
import dreipc.asset.search.models.MuseumObjectCountConnection;
import dreipc.asset.search.models.MuseumObjectIndex;
import dreipc.asset.search.repositories.MuseumObjectIndexRepository;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MuseumObjectIndexRepositoryStub implements MuseumObjectIndexRepository {


    @Override
    public Page<MuseumObjectIndex> searchSimilar(MuseumObjectIndex entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<MuseumObjectIndex> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<MuseumObjectIndex> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends MuseumObjectIndex> S save(S entity) {
        return null;
    }

    @Override
    public <S extends MuseumObjectIndex> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<MuseumObjectIndex> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<MuseumObjectIndex> findAll() {
        return null;
    }

    @Override
    public Iterable<MuseumObjectIndex> findAllById(Iterable<String> strings) {
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
    public void delete(MuseumObjectIndex entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends MuseumObjectIndex> entities) {

    }

    @Override
    public void deleteAll() {

    }


    @Override
    public MuseumObjectCountConnection searchMuseumObjects(MuseumObjectSearchWhereInput where, List<MuseumObjectSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return new MuseumObjectCountConnection(Collections.emptyList(), 0, null, Collections.emptyList());

    }

    @Override
    public MuseumObjectCountConnection<MuseumObjectIndex> exceptionalMuseumObjects(MuseumObjectSearchWhereInput where, List<MuseumObjectSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment) {
        return null;
    }

    @Override
    public List<String> smartKeywords(int first, int skip) {
        return List.of("Meer", "Kleidung", "Ruine", "Allee", "Buch");
    }



}
