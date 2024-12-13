package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.utils.other.FindClosestPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class AimAssist extends Module implements TickListener {

    private float yaw, pitch;

    public AimAssist() {
        super("Aim Assist", "Automatically aims at targets", 0, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.world != null) {
            PlayerEntity closestPlayer = FindClosestPlayer.findClosest(mc.player, 4);

            if (closestPlayer != null) {
                double diffX = closestPlayer.getX() - mc.player.getX();
                double diffY = closestPlayer.getY() + closestPlayer.getEyeHeight(closestPlayer.getPose()) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
                double diffZ = closestPlayer.getZ() - mc.player.getZ();
                double distance = MathHelper.sqrt((float) (diffX * diffX + diffZ * diffZ));

                yaw = (float) (MathHelper.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
                pitch = (float) -(MathHelper.atan2(diffY, distance) * 180.0D / Math.PI);

                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
            }
        }
    }
}