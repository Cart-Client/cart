package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.RangeSetting;
import com.ufo.cart.module.setting.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.client.network.ClientPlayerInteractionManager;

import java.util.Random;

public class TriggerBot extends Module implements TickListener {

    private final BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);
    private final NumberSetting cooldownProgress = new NumberSetting("Cooldown Progress", 0.0, 1.0, 1.0, 0.01);
    private final RangeSetting attackDelay = new RangeSetting("Attack Delay", 0, 20, 0, 1, 1);
    private final NumberSetting maceDelay = new NumberSetting("Mace Delay", 0.0, 100.0, 50.0, 1.0);
    private final BooleanSetting teamCheck = new BooleanSetting("Team Check", false);
    private int attackCooldown = 0;
    private final Random random = new Random();

    public TriggerBot() {
        super("Trigger Bot", "Automatically hits targets", 0, Category.COMBAT);
        addSettings(swordOnly, cooldownProgress, attackDelay, maceDelay, teamCheck);
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
        if (mc.player == null || mc.currentScreen != null) {
            return;
        }

        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }

        if (mc.crosshairTarget instanceof EntityHitResult hit && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            Entity target = hit.getEntity();

            if (target instanceof PlayerEntity && target.isAlive() && target != mc.player) {
                Item heldItem = mc.player.getMainHandStack().getItem();

                if (mc.player.isUsingItem()) {
                    return;
                }

                if (mc.player.getAttackCooldownProgress(cooldownProgress.getValueFloat()) < cooldownProgress.getValueFloat()) {
                    return;
                }

                int delay = 0;
                int attackDelayInterval = 0;

                if (attackDelay.getValueMaxInt() >= attackDelay.getValueMinInt()) {
                    attackDelayInterval = random.nextInt(attackDelay.getValueMinInt(), attackDelay.getValueMaxInt());
                }

                if (swordOnly.getValue()) {
                    if (!(heldItem instanceof SwordItem)) {
                        return;
                    }
                    delay = (int) (mc.player.getAttackCooldownProgress(1.0F) + attackDelayInterval);
                } else {
                    if (heldItem instanceof MaceItem) {
                        delay = (int) (maceDelay.getValue() + attackDelayInterval);
                    } else if (heldItem instanceof SwordItem || heldItem instanceof AxeItem) {
                        delay = mc.attackCooldown + attackDelayInterval;
                    } else {
                        delay = (int) (mc.player.getAttackCooldownProgress(1.0F) + attackDelayInterval);
                    }
                }

                if (teamCheck.getValue() && target.getTeamColorValue() == mc.player.getTeamColorValue()) {
                    return;
                }

                attackCooldown = delay;

                ClientPlayerInteractionManager interactionManager = mc.interactionManager;
                assert interactionManager != null;
                interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}