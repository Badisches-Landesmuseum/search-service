package dreipc.asset.search.queries;

import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public class NativeQueryBuilder {

    private String queryBody;
    private int from = 0;
    private int size = 0;

    @Builder.Default
    private Boolean trackTotalHits = true;

    public void addQuery(String queryBody) {
        this.queryBody = this.queryBody + ", " + queryBody;
    }

    public String getQueryBody() {
        return QUERY
                .replace("<QUERY_BODY>", queryBody)
                .replace("<TOTAL_HITS_TRACKER>", trackTotalHits.toString());
    }

    private final static String QUERY = """   
            {
             "size": 0,
             "track_total_hits": <TOTAL_HITS_TRACKER>,
              <QUERY_BODY>
            }
            """;


}
