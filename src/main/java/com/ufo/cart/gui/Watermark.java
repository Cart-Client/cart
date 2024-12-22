package com.ufo.cart.gui;

import com.ufo.cart.Client;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.module.modules.render.HUD;
import com.ufo.cart.utils.render.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

import static com.ufo.cart.utils.Utils.mc;

public class Watermark {

    private static final int BACKGROUND_COLOR = new Color(0, 0, 0, 150).getRGB();
    private static final int WATERMARK_X = 8;
    private static final int WATERMARK_Y = 12;


    public static  void renderShit(DrawContext context) {
        Theme themeModule = Client.getInstance().getModuleManager().getModule(Theme.class);
        renderWatermark(context, themeModule);
    }

    private static void renderWatermark(DrawContext context, Theme themeModule) {

        HUD hudModule = Client.getInstance().getModuleManager().getModule(HUD.class);
        Color textColor = Color.white;
        if (hudModule.textColor.getMode() == "White") {
            textColor = Color.white;
        }
        else {
            textColor = themeModule.getColor(0);
        }

        // watermark
        if (hudModule.isEnabled() && hudModule.watermark.getValue()) {
            Text playerName = mc.player.getName();
            String playerNameString = playerName.getString();
            String info = "Cart" + " | " + mc.getCurrentFps() + " fps" + " | " + playerNameString;
            int backgroundWidth = mc.textRenderer.getWidth(info);
            TextRenderer.drawSmallMinecraftText(info, context, WATERMARK_X, WATERMARK_Y, textColor.getRGB(), true);
            context.fill(WATERMARK_X / 2 - 2, WATERMARK_Y / 2 - 2, WATERMARK_X / 2 + backgroundWidth + 2, WATERMARK_Y / 2 - 4, themeModule.getColor(0).getRGB());
            context.fill(WATERMARK_X / 2 - 2, WATERMARK_Y / 2 - 2, WATERMARK_X / 2 + backgroundWidth + 2, WATERMARK_Y / 2 + mc.textRenderer.fontHeight + 2, BACKGROUND_COLOR);
        }
    }
}
