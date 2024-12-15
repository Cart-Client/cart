package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.modules.render.ArrayListModule;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import com.ufo.cart.utils.render.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.Objects;
import com.ufo.cart.module.modules.client.Theme;
import static com.ufo.cart.Client.mc;

public class Hud {
    private static final Class<? extends Module> ArrayList = ArrayListModule.class ;

    private static final float TEXT_SCALE = 0.75f; // adujsut for da size

    public static void render(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
    }

    public static void renderArrayList(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
        var enabledModules = Client.getInstance().getModuleManager().getEnabledModules();

        if (Objects.requireNonNull(Client.getInstance().getModuleManager().getModule(ArrayList)).isEnabled()) {
            enabledModules.sort((module1, module2) -> {
                int width1 = (int)(mc.textRenderer.getWidth(module1.getName()) * TEXT_SCALE);
                int width2 = (int)(mc.textRenderer.getWidth(module2.getName()) * TEXT_SCALE);
                return Integer.compare(width2, width1);
            });

            int yOffset = 5;
            int xOffset = 5;

            for (Module module : enabledModules) {
                 int width = (int)(mc.textRenderer.getWidth(module.getName()) * TEXT_SCALE);
                //context.fill(xOffset - 1, yOffset - 2, xOffset + width + 1, yOffset + 10, new Color(0, 0, 0, 150).getRGB());
                //context.fill(xOffset - 2, yOffset - 2, xOffset, yOffset + 10, ThemeUtils.getMainColor(255).getRGB());

                // if doesnt work scale text and adjust y offeset
                context.getMatrices().push();
                 context.getMatrices().scale(TEXT_SCALE, TEXT_SCALE, 1); // if doesnt work scale text matirx
                 int scaledY = (int) (yOffset / TEXT_SCALE); // if doesnt work addujst y pos for scaling

                context.fill((int)(xOffset / TEXT_SCALE - 1), scaledY - 2, (int)(xOffset / TEXT_SCALE + width + 1), scaledY + 10, new Color(0, 0, 0, 150).getRGB());
                context.fill((int)(xOffset / TEXT_SCALE - 2), scaledY - 2, (int)(xOffset / TEXT_SCALE), scaledY + 10, ThemeUtils.getMainColor(255).getRGB());
                TextRenderer.drawMinecraftText(module.getName(), context,(int) (xOffset / TEXT_SCALE),scaledY , ThemeUtils.getMainColor(255).getRGB(), true);
               context.getMatrices().pop();
                yOffset += 12;
            }
        }
    }
}
