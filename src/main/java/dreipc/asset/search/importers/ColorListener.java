package dreipc.asset.search.importers;

import dreipc.asset.search.ColorUtil;
import dreipc.asset.search.models.Color;
import dreipc.asset.search.models.ColorIndex;
import dreipc.proto.ColorExtractorProtos;
import dreipc.asset.search.repositories.ColorIndexRepository;
import dreipc.q8r.proto.asset.AssetProtos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ColorListener {

    private final ColorIndexRepository repository;
    public ColorListener(ColorIndexRepository repository) {
        this.repository = repository;
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.color.create", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "image.color.created"),
            containerFactory = "rabbitListenerContainerFactory")
    private void created(List<ColorExtractorProtos.ImageColorIndexAction> list) {
        var colorImages = list
                .stream()
                .map(elem -> {
                    var builder = ColorIndex
                            .builder()
                            .id(elem.getImageId())
                            .projectId(elem.getProjectId())
                            .artefactId(elem.getArtefactId());

                    var colorPalette = elem
                            .getColorInfo()
                            .getColorPaletteList()
                            .stream()
                            .map(color -> {
                                var hsl = ColorUtil.hex2HSL(color.getHexRGB());
                                return Color
                                        .builder()
                                        .rgbHex(color.getHexRGB())
                                        .hue(hsl[0])
                                        .saturation(hsl[1])
                                        .lightness(hsl[2])
                                        .ratio(color.getRatio())
                                        .name(color.getName())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    builder.palette(colorPalette);
                    return builder.build();
                })
                .toList();

        repository.saveAll(colorImages);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "page.similarities.image.delete", durable = "true",
            arguments = @Argument(name = "x-dead-letter-exchange", value = "assets-dlx")),
            exchange = @Exchange(value = "assets", type = "topic"), key = "image.deleted"),
            containerFactory = "rabbitListenerContainerFactory")
    private void deleted(List<AssetProtos.AssetDeletedEvent> list) {
        var assetIds = list
                .stream()
                .map(AssetProtos.AssetDeletedEvent::getId)
                .toList();
        repository.deleteAllById(assetIds);
    }
}
