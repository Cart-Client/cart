package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.NumberSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import com.ufo.cart.utils.other.PlayerUtil;

public class AimAssist extends Module implements Render3DListener {

    private float yaw, pitch;
    private final NumberSetting FOV = new NumberSetting("FOV", 0.0, 180.0, 180.0, 1.0);
    private final NumberSetting Range = new NumberSetting("Range", 0.0, 10.0, 3.0, 1.0);
    private final NumberSetting Smoothness = new NumberSetting("Smoothness", 0.0, 1.0, 1.0, 0.05);

    public AimAssist() {
        super("Aim Assist", "Automatically aims at targets", 0, Category.COMBAT);
        addSettings(FOV, Range, Smoothness);
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
    public void onRender(Render3DEvent event) {
        if (mc.player != null && mc.world != null) {
            PlayerEntity closestPlayer = PlayerUtil.findClosest(mc.player, Range.getValue());

            if (closestPlayer != null && mc.player.distanceTo(closestPlayer) <= Range.getValue()) {
                double diffX = closestPlayer.getX() - mc.player.getX();
                double diffY = (closestPlayer.getY() + closestPlayer.getEyeHeight(mc.player.getPose()) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose())));
                double diffZ = closestPlayer.getZ() - mc.player.getZ();

                float playerYaw = mc.player.getYaw();
                float playerPitch = mc.player.getPitch();

                double distance = MathHelper.sqrt((float) (diffX * diffX + diffZ * diffZ));

                float targetYaw = (float) (MathHelper.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
                float targetPitch = (float) -(MathHelper.atan2(diffY, distance) * 180.0D / Math.PI);

                float yawDiff = MathHelper.wrapDegrees(targetYaw - playerYaw);
                float pitchDiff = MathHelper.wrapDegrees(targetPitch - playerPitch);

                double angle = Math.toDegrees(Math.acos(MathHelper.clamp(Math.cos(Math.toRadians(yawDiff)) * Math.cos(Math.toRadians(pitchDiff)), -1.0, 1.0)));

                if (angle <= FOV.getValue()) {
                    float smoothingFactor = 1.0f - Smoothness.getValueFloat();

                    smoothingFactor = MathHelper.clamp(smoothingFactor, 0.1f, 1.0f);

                    float aimAssistYawAdjustment = yawDiff * smoothingFactor;
                    float aimAssistPitchAdjustment = pitchDiff * smoothingFactor;

                    yaw = mc.player.getYaw() + aimAssistYawAdjustment;
                    pitch = MathHelper.clamp(mc.player.getPitch() + aimAssistPitchAdjustment, -90.0F, 90.0F);

                    mc.player.setYaw(yaw);
                    mc.player.setPitch(pitch);
                }
            }
        }
    }
}