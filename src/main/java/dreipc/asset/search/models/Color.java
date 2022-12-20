package dreipc.asset.search.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Color {
    String name;
    String rgbHex;
    float hue;
    float saturation;
    float lightness;
    float ratio;
}
