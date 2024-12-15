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

    public static void render(RenderTickCounter tickDelta, DrawContext context, MatrixStack matrices) {
        // rendar logic
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

            for (Module module : enabledModules) {
                TextRenderer.drawMinecraftText(module.getName(), context, xOffset, yOffset, ThemeUtils.getMainColor(255).getRGB(), true);
                yOffset += 20;
            }
        }
    }
}
