package ipixelmon.pixelbay.gui;

import java.awt.*;

public class ColorPicker
{

    public static Color color(float r, float g, float b, float a) {
        return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

}
