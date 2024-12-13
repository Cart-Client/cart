package com.ufo.cart.utils.other;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public final class PlayerUtil {

    public static PlayerEntity findClosest(PlayerEntity sourcePlayer, double radius) {
        World world = sourcePlayer.getWorld();
        if (world == null) {
            return null;
        }

        PlayerEntity closestPlayer = null;
        double closestDistanceSquared = radius * radius;

        for (PlayerEntity targetPlayer : world.getPlayers()) {
            if (targetPlayer == sourcePlayer) {
                continue;
            }

            double distanceSquared = sourcePlayer.squaredDistanceTo(targetPlayer);

            if (distanceSquared < closestDistanceSquared) {
                closestPlayer = targetPlayer;
                closestDistanceSquared = distanceSquared;
            }
        }
        return closestPlayer;
    }
}