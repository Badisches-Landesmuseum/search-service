package dreipc.asset.search.models;

import dreipc.common.graphql.relay.CountConnection;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;

public class MuseumObjectColorResultConnection<T> extends CountConnection<T> {

    public MuseumObjectColorResultConnection(List<T> data, long totalCount, DataFetchingEnvironment environment) {
        super(data, totalCount, environment);

    }
}
