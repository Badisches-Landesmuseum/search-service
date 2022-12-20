package dreipc.asset.search.models;

import de.dreipc.q8r.asset.page.similarities.types.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Tolerate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(indexName = "xcurator-service.museumobject")
@Builder
@Component
public class MuseumObjectIndex {

    @Id
    @NonNull
    @Field(fielddata = true, type = FieldType.Keyword)
    String id;
    @Field(type = FieldType.Text)
    List<String> titles;
    @Field(type = FieldType.Text)
    List<String> descriptions;
    @Field(type = FieldType.Keyword)
    List<String> keywords;
    @Field(type = FieldType.Keyword)
    List<String> materials;
    @Field(type = FieldType.Date)
    Instant updatedAt;
    @Field(type = FieldType.Date)
    Instant createdAt;
    @Field(type = FieldType.Date)
    Instant earliestDate;
    @Field(type = FieldType.Date)
    Instant latestDate;
    @Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
    List<NamedEntityIndex> namedEntities = new ArrayList<>();
    @Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
    List<TopicIndex> topics = new ArrayList<>();
    @Field(type = FieldType.Keyword)
    String countryName;
    @Field(type = FieldType.Keyword)
    String epoch;
    @Tolerate
    public MuseumObjectIndex() {
    }

    public MuseumObject toSource() {
        return MuseumObject.newBuilder().id(id).build();
    }
}
