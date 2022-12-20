package dreipc.asset.search.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@Builder
public class NamedEntityIndex {
    @Id
    String id;
    @Field(type = FieldType.Keyword)
    @NonNull
    String literal;
    @Field(type = FieldType.Keyword)
    @NonNull
    String type;
    String knowledgeBaseId;
}
