package dreipc.asset.search.services;

import dreipc.asset.search.ColorUtil;
import dreipc.asset.search.models.Color;
import dreipc.asset.search.models.ColorIndex;
import dreipc.asset.search.repositories.ColorIndexRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorSeeder {

    private List<String> colors = List.of("#000000", "#FFFFFF", "#00b8b8", "#ff81ae");
    private final ColorIndexRepository repository;

    public ColorSeeder(ColorIndexRepository repository) {
        this.repository = repository;
    }

    public void seed() {
        repository.deleteAll();

        var data = new ArrayList<ColorIndex>();

        for (int i = 0; i < colors.size(); i++) {
            var hsl = ColorUtil.hex2HSL(colors.get(i));
            var color = Color.builder()
                                     .rgbHex(colors.get(i))
                                     .ratio((float) (Math.random() * 100.0))
                                     .hue(hsl[0])
                                     .saturation(hsl[1])
                                     .lightness(hsl[2])
                                     .build();
                   var colorIndex = ColorIndex
                    .builder()
                    .id(i + "")
                            .palette(List.of(color))
                    .projectId("test-project")
                    .artefactId(i + "-artefact")
                    .build();
            data.add(colorIndex);
        }

        repository.saveAll(data);
    }
}
