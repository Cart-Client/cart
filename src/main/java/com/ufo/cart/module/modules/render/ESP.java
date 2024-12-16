package com.ufo.cart.module.modules.render;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.utils.render.RenderUtils;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;


public class ESP extends Module implements Render3DListener {

    public ESP() {
        super("ESP", "Show enemies through walls.", 0, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.eventBus.registerPriorityListener(Render3DListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.eventBus.unregister(Render3DListener.class, this);
    }

    @Override
    public void onRender(Render3DEvent event) {
        if (mc.world != null && mc.player != null) {
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player != mc.player) {
                    MatrixStack matrices = event.matrices;
                    matrices.push();

                    double xPos = MathHelper.lerp(RenderTickCounter.ONE.getTickDelta(true), player.prevX, player.getX());
                    double yPos = MathHelper.lerp(RenderTickCounter.ONE.getTickDelta(true), player.prevY, player.getY());
                    double zPos = MathHelper.lerp(RenderTickCounter.ONE.getTickDelta(true), player.prevZ, player.getZ());


                    matrices.translate(xPos, yPos, zPos);


                    RenderUtils.renderFilledBox(
                            matrices,
                            - player.getWidth() / 2,
                            0,
                            - player.getWidth() / 2,
                            player.getWidth() / 2,
                            player.getHeight(),
                            player.getWidth() / 2,
                            ThemeUtils.getMainColor());
                    matrices.pop();
                }
            }
        }
    }
}
