package dreipc.asset.search.commands;


import de.dreipc.rabbitmq.ProtoUtil;
import dreipc.asset.search.models.PageIndex;
import dreipc.q8r.proto.asset.AssetProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreatPageIndexCommand implements Command {

    public PageIndex execute(AssetProtos.PageProto pageProto) {
        return PageIndex.builder()
                .id(pageProto.getId())
                .assetId(pageProto.getAssetId())
                .projectId(pageProto.getProjectId())
                .content(pageProto.getContent())
                .links(pageProto.getLinksList())
                .pageNumber(pageProto.getPageNumber())
                .createdAt(ProtoUtil.fromProto(pageProto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(pageProto.getUpdatedAt()))
                .build();
    }

    @Override
    public Operation getName() {
        return Operation.CREATE_PAGE_INDEX;
    }
}
