package dreipc.asset.search.testutil.stubs;

import dreipc.asset.search.models.ColorIndex;
import dreipc.asset.search.repositories.ColorIndexRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class ColorIndexRepositoryStub implements ColorIndexRepository {


    @Override
    public Page<ColorIndex> searchSimilar(ColorIndex entity, String[] fields, Pageable pageable) {
        return null;
    }

    @Override
    public Iterable<ColorIndex> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<ColorIndex> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ColorIndex> S save(S entity) {
        return null;
    }

    @Override
    public <S extends ColorIndex> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ColorIndex> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<ColorIndex> findAll() {
        return null;
    }

    @Override
    public Iterable<ColorIndex> findAllById(Iterable<String> strings) {
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
    public void delete(ColorIndex entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ColorIndex> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
