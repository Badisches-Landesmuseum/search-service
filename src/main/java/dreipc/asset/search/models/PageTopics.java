package dreipc.asset.search.models;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class PageTopics {

    @NonNull
    String pageId;
    List<String> topics;

}
