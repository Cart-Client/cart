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

    public AimAssist()
    {
        super("Aim Assist", "Automatically aims at targets", 0, Category.COMBAT);
        addSettings(FOV, Range);
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

            if (closestPlayer != null) {
                double diffX = closestPlayer.getX() - mc.player.getX();
                double diffY = closestPlayer.getY() + closestPlayer.getEyeHeight(closestPlayer.getPose()) -
                        (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
                double diffZ = closestPlayer.getZ() - mc.player.getZ();

                float playerYaw = mc.player.getYaw();
                float playerPitch = mc.player.getPitch();

                double playerDirX = -Math.cos(Math.toRadians(playerPitch)) * Math.sin(Math.toRadians(playerYaw));
                double playerDirY = -Math.sin(Math.toRadians(playerPitch));
                double playerDirZ = Math.cos(Math.toRadians(playerPitch)) * Math.cos(Math.toRadians(playerYaw));

                double targetDistance = MathHelper.sqrt((float) (diffX * diffX + diffY * diffY + diffZ * diffZ));
                double targetDirX = diffX / targetDistance;
                double targetDirY = diffY / targetDistance;
                double targetDirZ = diffZ / targetDistance;

                double dotProduct = (playerDirX * targetDirX) + (playerDirY * targetDirY) + (playerDirZ * targetDirZ);
                double angle = Math.toDegrees(Math.acos(dotProduct));

                float fov = (float) FOV.getValue();
                if (angle <= fov) {
                    yaw = (float) (MathHelper.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
                    pitch = (float) -(MathHelper.atan2(diffY, MathHelper.sqrt((float) (diffX * diffX + diffZ * diffZ))) * 180.0D / Math.PI);
                    mc.player.setYaw(yaw);
                    mc.player.setPitch(pitch);
                }
            }
        }
    }
}