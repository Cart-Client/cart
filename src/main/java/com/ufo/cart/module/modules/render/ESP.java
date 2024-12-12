package com.ufo.cart.module.modules.render;
import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import com.ufo.cart.utils.render.RenderUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.awt.*;

public final class ESP extends Module implements Render3DListener {
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
        super.onDisable();
    }


    @Override
    public void onRender3D(Render3DEvent event) {
        MatrixStack matrices = event.matrices;
        assert mc.world != null;
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != mc.player) {
                Camera cam = mc.getBlockEntityRenderDispatcher().camera;
                if (cam != null) {
                    matrices.push();
                    Vec3d vec = cam.getPos();
                    matrices.translate(-vec.x, -vec.y, -vec.z);
                }

                double xPos = MathHelper.lerp(RenderTickCounter.ONE.getTickDelta(true), player.prevX, player.getX());
                double yPos = MathHelper.lerp(RenderTickCounter.ONE.getTickDelta(true), player.prevY, player.getY());
                double zPos = MathHelper.lerp(RenderTickCounter.ONE.getTickDelta(true), player.prevZ, player.getZ());

                RenderUtils.renderFilledBox(
                        event.matrices,
                        (float) xPos - player.getWidth() / 2,
                        (float) yPos,
                        (float) zPos - player.getWidth() / 2,
                        (float) xPos + player.getWidth() / 2,
                        (float) yPos + player.getHeight(),
                        (float) zPos + player.getWidth() / 2,
                        Color.white);
                matrices.pop();
            }
        }
    }
}