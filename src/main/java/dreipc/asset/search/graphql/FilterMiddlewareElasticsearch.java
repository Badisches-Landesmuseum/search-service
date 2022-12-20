package dreipc.asset.search.graphql;

import de.dreipc.q8r.asset.page.similarities.types.Highlight;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Slf4j
@Component
public class FilterMiddlewareElasticsearch {

    private static final String TYPE_QUERY_INPUT_FIELD = "_typeName";
    private final List<String> specialFields = Arrays.asList(
            TYPE_QUERY_INPUT_FIELD    // Filter by GraphQL __typename
            // Query Text input
    );

    @NonNull
    public BoolQueryBuilder convert(Object filter, @NonNull Class<?> sourceClass) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        getNonNullFields(filter)
                .stream()
                .filter(field -> specialFields.contains(field.getName()))
                .forEach(field -> {
                    if (TYPE_QUERY_INPUT_FIELD.equals(field.getName())) {
                        var typeValue = getFieldValue(field, filter);
                        getNonNullFields(typeValue)
                                .stream()
                                .forEach(filterOperator -> {
                                    var filterValue = getFieldValue(filterOperator, typeValue);
                                    if (filterValue instanceof List<?> listValue) {
                                        listValue.stream().forEach(object -> {
                                            var filterQuery = getCriteria("assetType", filterOperator.getName(), filterValue);
                                            boolQueryBuilder.filter(filterQuery);

                                        });
                                    } else {
                                    }

                                });
                    }
                });
        return boolQueryBuilder;
    }

    private List<Field> getNonNullFields(Object filter) {
        var allFields = Arrays
                .stream(filter
                        .getClass()
                        .getDeclaredFields())
                .toList();
        allFields.forEach(field -> field.setAccessible(true));

        return allFields
                .stream()
                .filter(field -> Objects.nonNull(getFieldValue(field, filter)))
                .toList();
    }


    private static Object getFieldValue(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "Unable to get the field value of: " + field.getName() + ". Error: " + e.getMessage());
        }
    }


    private AbstractQueryBuilder getCriteria(String fieldName, String operatorName, Object filterValue) {

        return switch (operatorName) {
            case "in" -> buildInQuery(fieldName, filterValue);
            case "notIn" -> boolQuery().mustNot(matchQuery(fieldName, filterValue));
            case "equals" -> boolQuery().must(matchQuery(fieldName, filterValue));
            case "lt" -> rangeQuery(fieldName).lt(filterValue);
            case "lte" -> rangeQuery(fieldName).lte(filterValue);
            case "gt" -> rangeQuery(fieldName).gt(filterValue);
            case "gte" -> rangeQuery(fieldName).gte(filterValue);
            case "contains" -> matchQuery(fieldName, filterValue);
            case "startsWith" -> prefixQuery(fieldName, filterValue.toString());
            case "endsWith" -> regexpQuery(fieldName, ".*" + filterValue.toString() + "$");
            case "not" -> boolQuery().mustNot(matchQuery(fieldName, filterValue));
            default -> throw new IllegalArgumentException("Unknown Nested Filter operator: " + operatorName);
        };
    }

    private BoolQueryBuilder buildInQuery(String fieldName, Object filterValue) {

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (filterValue instanceof Collection<?> collectionValue) {
            collectionValue.forEach(object -> boolQueryBuilder.should(matchQuery(fieldName, object)));
        }
        return boolQueryBuilder;
    }


    public List<Highlight> toHighlights(Map<String, List<String>> highlights) {
        return highlights.entrySet().stream().map(entrySet -> Highlight.newBuilder().fieldName(entrySet.getKey()).content(entrySet.getValue()).build()).toList();
    }


}
