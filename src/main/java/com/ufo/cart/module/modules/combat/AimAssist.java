package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.NumberSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.MathHelper;
import com.ufo.cart.utils.other.PlayerUtil;

public class AimAssist extends Module implements Render3DListener {

    private float yaw, pitch;
    private float previousYaw, previousPitch;
    private final NumberSetting FOV = new NumberSetting("FOV", 0.0, 180.0, 180.0, 1.0);
    private final NumberSetting Range = new NumberSetting("Range", 0.0, 10.0, 3.0, 1.0);
    private final NumberSetting Smoothness = new NumberSetting("Smoothness", 0.0, 10.0, 10.0, 0.1);
    private final NumberSetting Strength = new NumberSetting("Strength", 0.0, 1.0, 0.5, 0.01);
    private final BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);
    private final BooleanSetting teamCheck = new BooleanSetting("Team Check", false);

    public AimAssist() {
        super("Aim Assist", "Automatically aims at targets", 0, Category.COMBAT);
        addSettings(FOV, Range, Smoothness, Strength, swordOnly, teamCheck);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(Render3DListener.class, this);
        previousYaw = mc.player.getYaw();
        previousPitch = mc.player.getPitch();
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

            Item heldItem = mc.player.getMainHandStack().getItem();

            if (swordOnly.getValue()) {
                if (!(heldItem instanceof SwordItem)) {
                    return;
                }
            }

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

                if (teamCheck.getValue() && closestPlayer.getTeamColorValue() == mc.player.getTeamColorValue()) {
                    return;
                }

                if (angle <= FOV.getValue()) {
                    float smoothingFactor = (float) Math.pow(0.5, Smoothness.getValueFloat() / 2.0);

                    float strengthFactor = Strength.getValueFloat();

                    float yawAdjustment = yawDiff * smoothingFactor * strengthFactor;
                    float pitchAdjustment = pitchDiff * smoothingFactor * strengthFactor;

                    yaw = mc.player.getYaw();
                    pitch = mc.player.getPitch();

                    yaw += yawAdjustment;
                    pitch += pitchAdjustment;

                    yaw = MathHelper.wrapDegrees(yaw);

                    pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

                    mc.player.setYaw(yaw);
                    mc.player.setPitch(pitch);

                    previousYaw = yaw;
                    previousPitch = pitch;

                } else {
                    previousYaw = mc.player.getYaw();
                    previousPitch = mc.player.getPitch();
                }
            } else {
                previousYaw = mc.player.getYaw();
                previousPitch = mc.player.getPitch();
            }
        }
    }
}