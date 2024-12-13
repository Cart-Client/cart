package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.RangeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.AxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.client.network.ClientPlayerInteractionManager;

import java.util.Random;

public class TriggerBot extends Module implements TickListener {

    private final BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);
    private final RangeSetting attackDelay = new RangeSetting("Attack Delay", 0, 20, 0, 1, 1);
    private int attackCooldown = 0;
    Random random = new Random();

    public TriggerBot() {
        super("Trigger Bot", "Automatically hits targets", 0, Category.COMBAT);
        addSettings(swordOnly, attackDelay);
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

            if (attackDelay.getValueMaxInt() != attackDelay.getValueMinInt()) {
                int attackDelayInterval = random.nextInt((attackDelay.getValueMaxInt() - attackDelay.getValueMinInt()));
            }

            if (target instanceof PlayerEntity && target.isAlive() && target != mc.player) {
                Item heldItem = mc.player.getMainHandStack().getItem();

                if (swordOnly.getValue()) {
                    if (!(heldItem instanceof SwordItem)) {
                        return;
                    }
                    attackCooldown = 16 + attackDelayInterval;
                } else {
                    if (heldItem instanceof SwordItem || heldItem instanceof AxeItem) {
                        attackCooldown = heldItem instanceof SwordItem ? 16 + attackDelayInterval : 16 + attackDelayInterval; // exmaple dleyas
                    } else {
                        attackCooldown = 4 + attackDelayInterval;
                    }
                }

                ClientPlayerInteractionManager interactionManager = mc.interactionManager;
                interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}
