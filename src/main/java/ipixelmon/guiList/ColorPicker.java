package ipixelmon.guiList;

import java.awt.*;

public final class ColorPicker {

    public static Color color(int r, int g, int b, int a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

}
