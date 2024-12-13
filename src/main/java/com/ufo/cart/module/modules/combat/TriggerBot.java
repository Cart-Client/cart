package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.AxeItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.client.network.ClientPlayerInteractionManager;

public class TriggerBot extends Module implements TickListener {

    private final BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);
    private int attackCooldown = 0;

    public TriggerBot() {
        super("Trigger Bot", "Automatically hits targets", 0, Category.COMBAT);
        addSetting(swordOnly);
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

                if (swordOnly.getValue()) {
                    if (!(heldItem instanceof SwordItem)) {
                        return;
                    }
                    attackCooldown = 20;
                } else {
                    if (heldItem instanceof SwordItem || heldItem instanceof AxeItem) {
                        attackCooldown = heldItem instanceof SwordItem ? 20 : 25; // exmaple dleyas
                    } else {
                        attackCooldown = 10; 
                    }
                }

                ClientPlayerInteractionManager interactionManager = mc.interactionManager;
                interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}
