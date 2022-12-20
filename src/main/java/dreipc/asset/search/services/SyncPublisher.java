package dreipc.asset.search.services;

import de.dreipc.rabbitmq.ProtoPublisher;
import dreipc.asset.search.models.AssetIndex;
import dreipc.asset.search.models.MuseumObjectIndex;
import dreipc.asset.search.models.PageIndex;
import dreipc.q8r.proto.document.PageSimilaritiesProtos;
import org.springframework.stereotype.Component;

@Component
public class SyncPublisher {


    private final ProtoPublisher protoPublisher;

    public SyncPublisher(ProtoPublisher protoPublisher) {
        this.protoPublisher = protoPublisher;
    }

    public void publish(AssetIndex assetIndex, String syncId) {
        var proto = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                .setAssetId(assetIndex.getId())
                .setProjectId(assetIndex.getProjectId())
                .setStatus(PageSimilaritiesProtos.StatusProto.DONE)
                .setSyncId(syncId)
                .build();
        protoPublisher.sendEvent("page.similarities.asset.imported", proto);

    }

    public void publish(MuseumObjectIndex index, String syncId) {
        var proto = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                .setAssetId(index.getId())
                .setStatus(PageSimilaritiesProtos.StatusProto.DONE)
                .setSyncId(syncId)
                .build();
        protoPublisher.sendEvent("page.similarities.museumobject.imported", proto);

    }


    public void publish(PageIndex index, String syncId) {
        var proto = PageSimilaritiesProtos.PageSimilaritiesIndexImportedEvent.newBuilder()
                .setAssetId(index.getAssetId())
                .setProjectId(index.getProjectId())
                .setStatus(PageSimilaritiesProtos.StatusProto.DONE)
                .setSyncId(syncId)
                .build();
        protoPublisher.sendEvent("page.similarities.page.imported", proto);

    }


}
