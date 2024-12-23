package com.ufo.cart.utils.render;

import com.ufo.cart.module.modules.client.Theme;
import java.awt.*;

public class ThemeUtils {

    public static Color getMainColor(int alpha) {
        int red = Theme.red.getValueInt();
        int green = Theme.green.getValueInt();
        int blue = Theme.blue.getValueInt();
        return new Color(red, green, blue, alpha);
    }

    public static Color getMainColor() {
        int red = Theme.red.getValueInt();
        int green = Theme.green.getValueInt();
        int blue = Theme.blue.getValueInt();
        return new Color(red, green, blue);
    }

    public static Color getMainColorReverse() {
        int red = Theme.red.getValueInt();
        int green = Theme.green.getValueInt();
        int blue = Theme.blue.getValueInt();
        return new Color(blue, green, red);
    }

    public static Color getTextColor() {
        int red = Theme.red.getValueInt();
        int green = Theme.green.getValueInt();
        int blue = Theme.blue.getValueInt();
        Color primaryColor = new Color(red, green, blue);

        if (isColorDark(primaryColor)) {
            return Color.WHITE;
        } else {
            return Color.LIGHT_GRAY;
        }
    }

    public static Color getBorderColor() {
        return new Color(40,40,40,255);
    }

    private static boolean isColorDark(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255.0;
        return luminance < 0.5;
    }
}