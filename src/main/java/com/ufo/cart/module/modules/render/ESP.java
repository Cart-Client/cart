package com.ufo.cart.module.modules.render;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.utils.render.Render3D;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;

public class ESP extends Module implements Render3DListener {

    private final NumberSetting opacity = new NumberSetting("Opacity", 0.0, 255.0, 50, 1);

    public ESP() {
        super("ESP", "Show enemies through walls.", 0, Category.RENDER);
        addSetting(opacity);
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
        if (mc.world == null || mc.player == null) return;

        MatrixStack matrices = event.matrices;
        Camera cam = mc.getBlockEntityRenderDispatcher().camera;
        if (cam == null) return;

        float fov = mc.options.getFov().getValue() + 45;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player || player.isRemoved()) continue;

            float tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false);
            Vec3d interpPos = Render3D.getEntityPositionInterpolated(player, tickDelta);

            if (isInFrustum(interpPos, cam, fov)) {
                matrices.push();

                Vec3d camPos = cam.getPos();

                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cam.getPitch()));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cam.getYaw() + 180.0F));
                matrices.translate(interpPos.x - camPos.x, interpPos.y - camPos.y, interpPos.z - camPos.z);

                Box playerBox = new Box(
                        -player.getWidth() / 2,
                        0,
                        -player.getWidth() / 2,
                        player.getWidth() / 2,
                        player.getHeight() + 0.1,
                        player.getWidth() / 2
                );

                Render3D.render3DBox(matrices, playerBox, ThemeUtils.getMainColor(), opacity.getValueInt(), 1f);
                matrices.pop();
            }
        }
    }

    private boolean isInFrustum(Vec3d pos, Camera cam, float fov) {
        Vec3d camPos = cam.getPos();
        Vec3d lookVec = Vec3d.fromPolar(cam.getPitch(), cam.getYaw());
        Vec3d toPos = pos.subtract(camPos);
        double dotProduct = toPos.normalize().dotProduct(lookVec);
        double angle = Math.toDegrees(Math.acos(dotProduct));
        return angle < fov / 2.0;
    }
}