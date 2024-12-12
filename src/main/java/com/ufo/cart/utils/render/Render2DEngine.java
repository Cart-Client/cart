package com.ufo.cart.utils.render;

import net.minecraft.util.math.MathHelper;

public class Render2DEngine {

    /**
     * Smoothly interpolates a value towards a target value.
     *
     * @param endPoint   The target value to interpolate towards.
     * @param current    The current value.
     * @param speed      The speed of interpolation (0.0 to 1.0).
     * @return           The interpolated value.
     */
    public static float scrollAnimate(float endPoint, float current, float speed) {
        boolean shouldContinueAnimation = endPoint > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0f) {
            speed = 1.0f;
        }

        float dif = Math.max(endPoint, current) - Math.min(endPoint, current);
        float factor = dif * speed;
        return current + (shouldContinueAnimation ? factor : -factor);
    }
}