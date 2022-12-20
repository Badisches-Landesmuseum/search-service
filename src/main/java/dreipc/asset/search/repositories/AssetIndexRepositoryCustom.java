package dreipc.asset.search.repositories;

import de.dreipc.q8r.asset.page.similarities.types.AssetSearchOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.AssetSearchWhereInput;
import de.dreipc.q8r.asset.page.similarities.types.PanquraArticleOrderByInput;
import de.dreipc.q8r.asset.page.similarities.types.PanquraArticleSearchWhereInput;
import dreipc.asset.search.models.AssetIndex;
import dreipc.asset.search.models.PanquraArticleIndexSearchResult;
import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public interface AssetIndexRepositoryCustom {

    CountConnection<AssetIndex> searchAssets(AssetSearchWhereInput where, List<AssetSearchOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment);

    CountConnection<PanquraArticleIndexSearchResult> searchWithHighlights(PanquraArticleSearchWhereInput where, List<PanquraArticleOrderByInput> orderBy, int first, int skip, DataFetchingEnvironment environment);


}
