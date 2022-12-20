package dreipc.asset.search.models;

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

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(indexName = "colors")
@Builder
@Component
public class ColorIndex {

    @Id
    @NonNull
    @Field(fielddata = true, type = FieldType.Keyword)
    String id;
    String rgbHex;
    @Field(type = FieldType.Keyword)
    String artefactId;
    @Field(type = FieldType.Keyword)
    String projectId;
    @Builder.Default
    @Field(type = FieldType.Nested, includeInParent = true)
    List<Color> palette = new ArrayList<>();


    @Tolerate
    public ColorIndex() {

    }
}
