package dreipc.asset.search.repositories;

import de.dreipc.q8r.asset.page.similarities.types.PageSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.PageSearchWhereInput;
import de.dreipc.q8r.asset.page.similarities.types.PageUniqueInput;
import dreipc.asset.search.models.PageIndex;
import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public interface PageIndexRepositoryCustom {
    CountConnection<PageIndex> searchPages(PageSearchWhereInput where, List<PageSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment);

    CountConnection<PageIndex> searchSimilarPages(PageUniqueInput where, List<PageSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment);

}
