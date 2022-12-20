package dreipc.asset.search.commands;


import de.dreipc.rabbitmq.ProtoUtil;
import dreipc.asset.search.models.MuseumObjectIndex;
import dreipc.xcurator.proto.XCuratorProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateMuseumObjectIndexCommand implements Command {


    public MuseumObjectIndex execute(XCuratorProtos.MuseumObjectProto proto) {
        return MuseumObjectIndex.builder()
                .id(proto.getId())
                .titles(proto.getTitlesList().stream().map(XCuratorProtos.TextContentProto::getContent).toList())
                .descriptions(proto.getDescriptionsList().stream().map(XCuratorProtos.TextContentProto::getContent).toList())
                .keywords(proto.getKeywordsList())
                .materials(proto.getMaterialsList())
                .createdAt(ProtoUtil.fromProto(proto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(proto.getUpdatedAt()))
                .countryName(proto.getLocation().getCountryName())
                .epoch(proto.getDateRange().getEpoch())
                .build();
    }


    @Override
    public Operation getName() {
        return Operation.CREATE_MUSEUM_OBJECT_INDEX;
    }
}
