package dreipc.asset.search.repositories;

import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.MuseumObjectSearchWhereInput;
import dreipc.asset.search.models.MuseumObjectCountConnection;
import dreipc.asset.search.models.MuseumObjectIndex;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public interface MuseumObjectIndexRepositoryCustom {

    MuseumObjectCountConnection<MuseumObjectIndex> searchMuseumObjects(MuseumObjectSearchWhereInput where, List<MuseumObjectSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment);

    MuseumObjectCountConnection <MuseumObjectIndex> exceptionalMuseumObjects(MuseumObjectSearchWhereInput where, List<MuseumObjectSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment);

    List<String> smartKeywords(int first, int skip);


}
