package dreipc.asset.search.services;

import dreipc.asset.search.ColorUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ColorUtilTest {

    @ParameterizedTest
    @CsvSource({
            "#FFFFFF, 0, 0, 100",
            "#000000, 0, 0, 0",
            "#0000FF, 240, 100, 50"
    })
    void rgbHex2HLS(String rgbHex, double hue, double satuation, double lightness) {
        var convertedHSL = ColorUtil.hex2HSL(rgbHex);
        assertNotNull(convertedHSL);
        assertEquals(convertedHSL[0], hue);
        assertEquals(convertedHSL[1], satuation);
        assertEquals(convertedHSL[2], lightness);
    }

    @ParameterizedTest
    @CsvSource({
            "#FFFFFF, 255, 255, 255",
            "#000000, 0, 0, 0"
    })
    void rgbHex2RGB(String rgbHex, double red, double green, double blue) {
        var convertedRGB = ColorUtil.hex2RGB(rgbHex);
        assertNotNull(convertedRGB);
        assertEquals(red, convertedRGB[0]);
        assertEquals(green, convertedRGB[1]);
        assertEquals(blue, convertedRGB[2]);
    }
}
