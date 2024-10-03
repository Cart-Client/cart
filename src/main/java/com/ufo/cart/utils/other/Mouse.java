package com.ufo.cart.utils.other;

import com.ufo.cart.Client;
import com.ufo.cart.mixin.MinecraftClientAccessor;
import com.ufo.cart.mixin.MouseHandlerAccessor;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Mouse {
    public static final HashMap<Integer, Boolean> inputs = new HashMap<>();

    public static final ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static MouseHandlerAccessor getMouseHandler() {
        return (MouseHandlerAccessor) ((MinecraftClientAccessor) Client.mc).getMouse();
    }

    public static boolean isKeyPressed(final int keyCode) {
        Boolean isPressed = inputs.get(keyCode);
        return isPressed != null && isPressed;
    }

    public static void simulateClick(final int keyCode) {
        inputs.put(keyCode, true);
        getMouseHandler().press(Client.mc.getWindow().getHandle(), keyCode, 1, 0);
    }

    public static void releaseKey(final int keyCode) {
        inputs.put(keyCode, false);
        getMouseHandler().press(Client.mc.getWindow().getHandle(), keyCode, 0, 0);
    }

    public static void pressKeyWithDelay(final int keyCode, final int delayMillis) {
        executorService.submit(() -> {
            try {
                simulateClick(keyCode);
                Thread.sleep(delayMillis);
                releaseKey(keyCode);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }

    public static void pressKeyDefaultDelay(final int keyCode) {
        pressKeyWithDelay(keyCode, 35);
    }
}

