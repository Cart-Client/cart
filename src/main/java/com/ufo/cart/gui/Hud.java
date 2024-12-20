package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.render.HUD;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import com.ufo.cart.utils.render.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.awt.*;

import static com.ufo.cart.Client.mc;

public class Hud {
    private static final Class<? extends Module> ArrayList = HUD.class;

    public static void renderArrayList(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
        var enabledModules = Client.getInstance().getModuleManager().getEnabledModules();
        HUD luhCalmHud = Client.getInstance().getModuleManager().getModule(HUD.class);

        assert luhCalmHud != null;
        if (luhCalmHud.isEnabled() && luhCalmHud.arrayList.getValue()) {
            enabledModules.sort((module1, module2) -> {
                int width1 = mc.textRenderer.getWidth(module1.getName());
                int width2 = mc.textRenderer.getWidth(module2.getName());
                return Integer.compare(width2, width1);
            });

            float initialHue = (System.currentTimeMillis() % 3600) / 3600f;
            float hue = initialHue;

            Color titleColor;
            if (luhCalmHud.colorMode.getMode() == "Rainbow") {
                titleColor = Color.getHSBColor(initialHue, 1.0f, 1.0f);
            } else {
                titleColor = ThemeUtils.getMainColor(255);
            }

            if (luhCalmHud.whichSide.getMode() == "Left") {
                int yOffset = 10;
                int xOffset = 10;

                TextRenderer.drawMediumMinecraftText("Cart v0.1", context, 8, 10, titleColor.getRGB(), true);
                yOffset += 13;

                for (Module module : enabledModules) {
                    int width = mc.textRenderer.getWidth(module.getName());
                    Color color;
                    if (luhCalmHud.colorMode.getMode() == "Rainbow") {
                        color = Color.getHSBColor(hue, 1.0f, 1.0f);
                        hue += 0.01f;
                        if (hue > 1.0f) hue -= 1.0f;
                    } else {
                        color = ThemeUtils.getMainColor(255);
                    }

                    context.fill(xOffset - 2, yOffset - 2, xOffset + width + 2, yOffset + 10, new Color(0, 0, 0, 150).getRGB());
                    context.fill(xOffset - 4, yOffset - 2, xOffset - 4 + 2, yOffset + 10, color.getRGB());
                    TextRenderer.drawSmallMinecraftText(module.getName(), context, xOffset * 2, yOffset * 2, color.getRGB(), true);
                    yOffset += 12;
                }
            } else if (luhCalmHud.whichSide.getMode() == "Right") {
                int yOffset = 10;
                int screenWidth = mc.getWindow().getScaledWidth();

                TextRenderer.drawMediumMinecraftText("Cart v0.1", context, 8, 10, titleColor.getRGB(), true);

                for (Module module : enabledModules) {
                    int width = mc.textRenderer.getWidth(module.getName());
                    int xOffset = screenWidth - width - 10;
                    Color color;
                    if (luhCalmHud.colorMode.getMode() == "Rainbow") {
                        color = Color.getHSBColor(hue, 1.0f, 1.0f);
                        hue += 0.01f;
                        if (hue > 1.0f) hue -= 1.0f;
                    } else {
                        color = ThemeUtils.getMainColor(255);
                    }

                    context.fill(xOffset - 2, yOffset - 2, xOffset + width + 2, yOffset + 10, new Color(0, 0, 0, 150).getRGB());
                    context.fill(xOffset + width + 2, yOffset - 2, xOffset + width + 4, yOffset + 10, color.getRGB());
                    TextRenderer.drawSmallMinecraftText(module.getName(), context, xOffset * 2, yOffset * 2, color.getRGB(), true);
                    yOffset += 12;
                }
            }
        }
    }
}