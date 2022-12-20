package dreipc.asset.search.services;

import dreipc.asset.search.queries.IdQueryBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

class IdQueryBuilderTest {

    @Test
    void testFilterId() {
        List<String> ids = List.of("638cec4c8e114672ed9820df", "638cec4c8e114672ed9820da");
        var query = IdQueryBuilder.query(ids);
        assert(query.contains("\"638cec4c8e114672ed9820df\", \"638cec4c8e114672ed9820da\""));
        assert(query.contains("]"));
        assert(query.contains("["));


    }

}
