package com.ufo.cart.module.modules.render;
import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.gui.colors.Color;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.utils.render.Render3D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import com.ufo.cart.utils.render.RenderUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.awt.*;

import static com.ufo.cart.Client.getInstance;

public final class ESP extends Module implements Render3DListener {
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    public ESP() {
        super("ESP", "Renders players through walls.", 0, Category.RENDER);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(Render3DListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(Render3DListener.class, this);
        super.onEnable();
    }

    @Override
    public void onRender(Render3DEvent event) {
        for (AbstractClientPlayerEntity entity : MC.world.getPlayers()) {
            if (entity != MC.player) {
                Render3D.draw3DBox(event.GetMatrix(), entity.getBoundingBox(), Color.convertHextoRGB("FFFFFF"));
            }
        }
    }
}