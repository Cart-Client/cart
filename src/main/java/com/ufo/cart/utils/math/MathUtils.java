package com.ufo.cart.utils.math;

import com.ufo.cart.utils.Utils;

public final class MathUtils implements Utils {
    public static double roundToDecimal(double n, double point) {
        return point * Math.round(n / point);
    }
}
