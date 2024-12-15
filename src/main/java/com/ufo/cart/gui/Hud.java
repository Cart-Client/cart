package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.render.ArrayListModule;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.Objects;
import static com.ufo.cart.Client.mc;

public class Hud {
    private static final Class<? extends Module> ArrayList = ArrayListModule.class;

    public static void render(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
    }

    public static void renderArrayList(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
        var enabledModules = Client.getInstance().getModuleManager().getEnabledModules();

        if (Objects.requireNonNull(Client.getInstance().getModuleManager().getModule(ArrayList)).isEnabled()) {
            enabledModules.sort((module1, module2) -> {
                int width1 = mc.textRenderer.getWidth(module1.getName());
                int width2 = mc.textRenderer.getWidth(module2.getName());
                return Integer.compare(width2, width1);
            });

            int yOffset = 10;
            int xOffset = 10;
            int lineHeight = mc.textRenderer.fontHeight + 4;
            int padding = 5;
            int totalHeight = enabledModules.size() * lineHeight + padding * 2;

            int longestWidth = 0;
            for (Module module : enabledModules) {
                int width = mc.textRenderer.getWidth(module.getName());
                if (width > longestWidth) {
                    longestWidth = width;
                }
            }

            int totalWidth = longestWidth + padding * 2;


            context.fill(xOffset - padding, yOffset - padding, xOffset + totalWidth, yOffset + totalHeight - padding, new Color(0, 0, 0, 150).getRGB());

            
            int borderColor = ThemeUtils.getMainColor(255).getRGB();
            context.fill(xOffset - padding, yOffset - padding, xOffset - padding + 1, yOffset + totalHeight - padding, borderColor);
            context.fill(xOffset + totalWidth - 1, yOffset - padding, xOffset + totalWidth, yOffset + totalHeight - padding, borderColor);
            context.fill(xOffset - padding, yOffset - padding, xOffset + totalWidth, yOffset - padding + 1, borderColor); 
            context.fill(xOffset - padding, yOffset + totalHeight - padding - 1, xOffset + totalWidth, yOffset + totalHeight - padding, borderColor); 
            for (Module module : enabledModules) {
                int textColor = ThemeUtils.getMainColor(255).getRGB();

                context.drawText(mc.textRenderer, module.getName(), xOffset + padding, yOffset + padding, textColor, true);
                yOffset += lineHeight;
            }
        }
    }
}
