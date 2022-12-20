package dreipc.asset.search.models;

import de.dreipc.q8r.asset.page.similarities.types.Highlight;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class PanquraArticleIndexSearchResult {

    AssetIndex index;
    List<Highlight> highlights;
}
