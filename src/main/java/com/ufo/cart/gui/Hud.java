package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.render.HUD;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import com.ufo.cart.utils.render.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.List;

import static com.ufo.cart.Client.mc;

public class Hud {

    public static void renderArrayList(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
        HUD hudModule = Client.getInstance().getModuleManager().getModule(HUD.class);

        if (hudModule == null || !hudModule.isEnabled() || !hudModule.arrayList.getValue()) return;

        List<Module> enabledModules = Client.getInstance().getModuleManager().getEnabledModules();
        enabledModules.sort((m1, m2) -> mc.textRenderer.getWidth(m2.getName()) - mc.textRenderer.getWidth(m1.getName()));

        float hue = (System.currentTimeMillis() % 3600) / 3600f;
        Color titleColor = hudModule.colorMode.getMode() == "Rainbow" ? Color.getHSBColor(hue, 1.0f, 1.0f) : ThemeUtils.getMainColor(255);

        TextRenderer.drawMediumMinecraftText("Cart v0.1", context, 8, 10, titleColor.getRGB(), true);

        int yOffset = 23;
        for (Module module : enabledModules) {
            renderModule(context, module, hudModule, hue, yOffset);
            yOffset += 12;
            if (hudModule.colorMode.getMode() == "Rainbow") hue = (hue + 0.01f) % 1.0f;
        }
    }

    private static void renderModule(DrawContext context, Module module, HUD hudModule, float hue, int yOffset) {
        String side = hudModule.whichSide.getMode();
        int screenWidth = mc.getWindow().getScaledWidth();
        int textWidth = mc.textRenderer.getWidth(module.getName());

        int xOffset = side.equals("Left") ? 10 : screenWidth - textWidth - 10;
        Color color = hudModule.colorMode.getMode() == "Rainbow" ? Color.getHSBColor(hue, 1.0f, 1.0f) : ThemeUtils.getMainColor(255);
        if (side.equals("Right")) { yOffset -= 13; }

        context.fill(xOffset - 2, yOffset - 2, xOffset + textWidth + 2, yOffset + 10, new Color(0, 0, 0, 150).getRGB());
        context.fill(
                side.equals("Left") ? xOffset - 4 : xOffset + textWidth + 2,
                yOffset - 2,
                side.equals("Left") ? xOffset - 2 : xOffset + textWidth + 4,
                yOffset + 10,
                color.getRGB()
        );
        TextRenderer.drawSmallMinecraftText(module.getName(), context, xOffset * 2, yOffset * 2, color.getRGB(), true);
    }
}