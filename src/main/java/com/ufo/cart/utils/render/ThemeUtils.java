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
}