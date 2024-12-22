package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.module.modules.render.HUD;
import net.minecraft.client.gui.DrawContext;

import com.ufo.cart.utils.render.TextRenderer;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

import static com.ufo.cart.Client.mc;

public class Hud {

    private static final int BACKGROUND_COLOR = new Color(0, 0, 0, 150).getRGB();
    private static final int MODULE_PADDING = 2;
    private static final int MODULE_HEIGHT = 10;
    private static final int MODULE_SPACING = MODULE_HEIGHT + MODULE_PADDING;
    private static final int SIDE_BAR_WIDTH = 2;
    private static final int SIDE_BAR_OFFSET = SIDE_BAR_WIDTH + MODULE_PADDING;
    private static final int WATERMARK_X = 8;
    private static final int WATERMARK_Y = 10;
    private static final int MODULE_START_Y = 23;

    public static void renderArrayList(DrawContext context) {
        HUD hudModule = Client.getInstance().getModuleManager().getModule(HUD.class);
        Theme themeModule = Client.getInstance().getModuleManager().getModule(Theme.class);

        if (hudModule == null || !hudModule.isEnabled() || !hudModule.arrayList.getValue()) return;
        if (themeModule == null) return;

        renderWatermark(context, themeModule);
        renderModuleList(context, hudModule, themeModule);
    }

    private static void renderWatermark(DrawContext context, Theme themeModule) {
        TextRenderer.drawMediumMinecraftText("Cart", context, WATERMARK_X, WATERMARK_Y, themeModule.getColor(0).getRGB(), true);
    }

    private static void renderModuleList(DrawContext context, HUD hudModule, Theme themeModule) {
        List<Module> enabledModules = Client.getInstance().getModuleManager().getEnabledModules();
        enabledModules.sort(Comparator.comparingInt(m -> -mc.textRenderer.getWidth(m.getName())));

        String side = hudModule.whichSide.getMode();
        int screenWidth = mc.getWindow().getScaledWidth();

        int yOffset = MODULE_START_Y;
        for (int i = 0; i < enabledModules.size(); i++) {
            Module module = enabledModules.get(i);
            renderModule(context, module, side, screenWidth, yOffset, themeModule.getColor(i));
            yOffset += MODULE_SPACING;
        }
    }

    private static void renderModule(DrawContext context, Module module, String side, int screenWidth, int yOffset, Color moduleColor) {
        int textWidth = mc.textRenderer.getWidth(module.getName());

        int x;
        if (side.equals("Left")) {
            x = 10;
        } else {
            x = screenWidth - textWidth - 10;
            yOffset -= 13;
        }

        // background
        context.fill(x - MODULE_PADDING, yOffset - MODULE_PADDING, x + textWidth + MODULE_PADDING, yOffset + MODULE_HEIGHT, BACKGROUND_COLOR);

        // sidebar
        context.fill(
                side.equals("Left") ? x - SIDE_BAR_OFFSET : x + textWidth + MODULE_PADDING,
                yOffset - MODULE_PADDING,
                side.equals("Left") ? x - MODULE_PADDING : x + textWidth + SIDE_BAR_OFFSET,
                yOffset + MODULE_HEIGHT,
                moduleColor.getRGB()
        );

        // modules
        TextRenderer.drawSmallMinecraftText(module.getName(), context, x * 2, yOffset * 2, moduleColor.getRGB(), true);
    }
}