package dreipc.asset.search.models;

import de.dreipc.q8r.asset.page.similarities.types.Node;
import de.dreipc.q8r.asset.page.similarities.types.Page;
import lombok.*;
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
@Document(indexName = "page")
@Builder
@Component
public class PageIndex implements Node {

    @Id
    @NonNull
    @Field(fielddata = true, type = FieldType.Keyword)
    String id;
    @NonNull
    @Field(type = FieldType.Text)
    String content;
    @NonNull
    String projectId;
    @NonNull
    String assetId;
    @Builder.Default
    List<String> links = new ArrayList<>();
    int pageNumber;
    @Getter
    long version;
    @Builder.Default
    @Field(type = FieldType.Nested)
    public List<NamedEntityIndex> namedEntities = new ArrayList<>();
    @Field(type = FieldType.Keyword)
    @Builder.Default
    List<String> tags = new ArrayList<>();
    @Field(type = FieldType.Keyword)
    List<String> topics;
    @Field(type = FieldType.Date)
    Instant createdAt;
    @Field(type = FieldType.Date)
    Instant updatedAt;


    public List<String> getPageTags() {
        return (tags == null) ? new ArrayList<>() : tags;
    }

    @Tolerate
    public PageIndex() {
    }

    public Page toSource() {
        return Page.newBuilder().id(id).build();
    }

}
