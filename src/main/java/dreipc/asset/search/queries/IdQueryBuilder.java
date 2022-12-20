package dreipc.asset.search.queries;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IdQueryBuilder {

    public static String query(List<String> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                throw new NullPointerException();
            }
            var withQuotationMarks = ids.stream().map(id -> '"' + id + '"').toList();
            return QUERY.replace("<ID_LIST>", withQuotationMarks.toString());
        } catch (Exception e) {
            log.info("Did not apply ids filter, cause:", e);
            throw e;
        }


    }

    private final static String QUERY = """
            "query": {
              "bool": {
                  "should": {
                      "terms":\s
                          {"_id": <ID_LIST> }
                  }
              }
            }
            """;
}
