package dreipc.asset.search.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class TopicIndex {
    @Id
    String id;
    Float weight;
    @Builder.Default
    @Field(type = FieldType.Keyword, includeInParent = true)
    List<String> topics = new ArrayList<>();

}
