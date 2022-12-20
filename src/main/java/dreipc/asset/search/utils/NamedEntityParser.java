package dreipc.asset.search.utils;

import dreipc.asset.search.models.NamedEntityIndex;
import dreipc.q8r.proto.asset.document.NamedEntitiesProtos;
import org.springframework.stereotype.Component;

@Component
public class NamedEntityParser {

    public NamedEntityIndex parse(NamedEntitiesProtos.NamedEntityProto namedEntityProto) {
        return NamedEntityIndex.builder()
                .id(namedEntityProto.getId())
                .literal(namedEntityProto.getLiteral())
                .type(namedEntityProto.getType())
                .knowledgeBaseId(namedEntityProto.getKnowledgeBaseId())
                .build();
    }


}
