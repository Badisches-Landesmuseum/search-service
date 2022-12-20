package dreipc.asset.search.models;

import de.dreipc.q8r.asset.page.similarities.types.*;
import dreipc.common.graphql.exception.NotSupportedException;
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
@Document(indexName = "asset")
@Builder
@Component
public class AssetIndex {

    @Id
    @NonNull
    @Field(fielddata = true, type = FieldType.Keyword)
    String id;
    String projectId;
    @Field(fielddata = true, type = FieldType.Text)
    String fileName;
    @Field(type = FieldType.Text)
    String title;
    @Field(fielddata = true, type = FieldType.Text)
    String content;
    @Field(fielddata = true, type = FieldType.Text)
    String shortAbstract;
    @Field(type = FieldType.Keyword)
    List<String> keywords;
    @Field(type = FieldType.Date)
    Instant updatedAt;
    @Field(type = FieldType.Date)
    Instant createdAt;
    AssetType assetType;
    @Builder.Default
    @Field(type = FieldType.Nested)
    public List<NamedEntityIndex> namedEntities = new ArrayList<>();

    @Tolerate
    public AssetIndex() {
    }

    public Asset toSource() {
        switch (this.getAssetType()) {
            case VIDEO:
                return Video.newBuilder().id(this.id).build();
            case IMAGE:
                return Image.newBuilder().id(this.id).build();
            case DOCUMENT:
                return de.dreipc.q8r.asset.page.similarities.types.Document.newBuilder().id(this.id).build();
            case AUDIO:
                return Audio.newBuilder().id(this.id).build();
            case HYPERTEXT:
                return Hypertext.newBuilder().id(this.id).build();
        }
        throw new NotSupportedException("Asset Type is not supported, add it to graphql schema if exists");
    }
}
