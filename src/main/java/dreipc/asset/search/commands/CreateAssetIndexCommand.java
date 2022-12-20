package dreipc.asset.search.commands;


import de.dreipc.q8r.asset.page.similarities.types.AssetType;
import de.dreipc.rabbitmq.ProtoUtil;
import dreipc.asset.search.models.AssetIndex;
import dreipc.q8r.proto.asset.AssetProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateAssetIndexCommand implements Command {


    public AssetIndex execute(AssetProtos.DocumentProto proto) {
        return AssetIndex.builder()
                .id(proto.getId())
                .projectId(proto.getProjectId())
                .fileName(proto.getFile().getName())
                .title(proto.getDescription().getTitle())
                .shortAbstract(proto.getDescription().getShortAbstract())
                .keywords(proto.getDescription().getKeywordsList())
                .createdAt(ProtoUtil.fromProto(proto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(proto.getUpdatedAt()))
                .assetType(AssetType.DOCUMENT)
                .build();
    }


    public AssetIndex execute(AssetProtos.VideoProto proto) {
        return AssetIndex.builder()
                .id(proto.getId())
                .projectId(proto.getProjectId())
                .fileName(proto.getFile().getName())
                .title(proto.getDescription().getTitle())
                .shortAbstract(proto.getDescription().getShortAbstract())
                .keywords(proto.getDescription().getKeywordsList())
                .createdAt(ProtoUtil.fromProto(proto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(proto.getUpdatedAt()))
                .assetType(AssetType.VIDEO)
                .build();
    }

    public AssetIndex execute(AssetProtos.AudioProto proto) {
        return AssetIndex.builder()
                .id(proto.getId())
                .projectId(proto.getProjectId())
                .fileName(proto.getFile().getName())
                .title(proto.getDescription().getTitle())
                .shortAbstract(proto.getDescription().getShortAbstract())
                .keywords(proto.getDescription().getKeywordsList())
                .createdAt(ProtoUtil.fromProto(proto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(proto.getUpdatedAt()))
                .assetType(AssetType.AUDIO)
                .build();
    }


    public AssetIndex execute(AssetProtos.HypertextProto proto) {
        return AssetIndex.builder()
                .id(proto.getId())
                .projectId(proto.getProjectId())
                .title(proto.getDescription().getTitle())
                .shortAbstract(proto.getDescription().getShortAbstract())
                .keywords(proto.getDescription().getKeywordsList())
                .createdAt(ProtoUtil.fromProto(proto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(proto.getUpdatedAt()))
                .assetType(AssetType.HYPERTEXT)
                .content(proto.getContent())
                .build();
    }

    public AssetIndex execute(AssetProtos.ImageProto proto) {
        return AssetIndex.builder()
                .id(proto.getId())
                .projectId(proto.getProjectId())
                .fileName(proto.getFile().getName())
                .title(proto.getDescription().getTitle())
                .shortAbstract(proto.getDescription().getShortAbstract())
                .keywords(proto.getDescription().getKeywordsList())
                .createdAt(ProtoUtil.fromProto(proto.getCreatedAt()))
                .updatedAt(ProtoUtil.fromProto(proto.getUpdatedAt()))
                .assetType(AssetType.IMAGE)
                .build();
    }

    @Override
    public Operation getName() {
        return Operation.CREATE_ASSET_INDEX;
    }
}
