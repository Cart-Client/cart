package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.events.ItemUseEvent;
import com.ufo.cart.event.listeners.ItemUseListener;
import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.utils.math.*;
import com.ufo.cart.utils.other.*;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public final class AutoCrystal extends Module implements TickListener, ItemUseListener {
    private final NumberSetting field133;
    private final NumberSetting field134;
    private final NumberSetting field135;
    private final NumberSetting field136;
    private final BooleanSetting field137;
    private final BooleanSetting field138;
    private final BooleanSetting field139;
    private final BooleanSetting field140;
    private final NumberSetting field141;
    private int field142;
    private int field143;

    public AutoCrystal() {
        super("Auto Crystal", "Automatically crystals fast for you", 0, Category.COMBAT);
        this.field133 = new NumberSetting("Place Delay", 0.0, 20.0, 0.0, 1.0);
        this.field134 = new NumberSetting("Break Delay", 0.0, 20.0, 0.0, 1.0);
        this.field135 = new NumberSetting("Place Chance", 0.0, 100.0, 100.0, 1.0);
        this.field136 = new NumberSetting("Break Chance", 0.0, 100.0, 100.0, 1.0);
        this.field137 = new BooleanSetting("Stop on Kill", false);
        this.field138 = new BooleanSetting("Fake Punch", false);
        this.field139 = new BooleanSetting("Click Simulation", false);
        this.field140 = new BooleanSetting("Damage Tick", false);
        this.field141 = new NumberSetting("Particle Chance", 0.0, 100.0, 20.0, 1.0);
        this.addSettings(new Setting[]{this.field133, this.field134, this.field135, this.field136, this.field137, this.field138, this.field139, this.field140, this.field141});
    }

    private static boolean method123(final AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return abstractClientPlayerEntity.hurtTime >= 2;
    }

    private static boolean method124(final AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return !abstractClientPlayerEntity.isOnGround();
    }

    private static boolean method125(final AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return abstractClientPlayerEntity.getLastAttacker() == null;
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        this.eventBus.registerPriorityListener(ItemUseListener.class, this);
        this.field142 = 0;
        this.field143 = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        this.eventBus.unregister(ItemUseListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        if (this.mc.currentScreen != null) {
            return;
        }
        final boolean b = this.field142 != 0;
        final boolean b2 = this.field143 != 0;
        if (this.field137.getValue() && TargetUtil.isDeadNearby()) {
            return;
        }
        final int method402 = RandomUtil.getRandom(1, 100);
        if (b) {
            --this.field142;
        }
        if (b2) {
            --this.field143;
        }
        if (this.mc.currentScreen != null) {
            return;
        }
        if (this.mc.player.isUsingItem()) {
            return;
        }
        if (this.field140.getValue() && this.method122()) {
            return;
        }
        if (GLFW.glfwGetMouseButton(this.mc.getWindow().getHandle(), 1) != 1) {
            return;
        }
        if (this.mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL) {
            return;
        }
        final HitResult crosshairTarget = this.mc.crosshairTarget;
        if (crosshairTarget instanceof final BlockHitResult blockHit) {
            if (this.mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                if (!b && method402 <= this.field135.getValueInt() && (BlockUtil.isBlockType(blockHit.getBlockPos(), Blocks.OBSIDIAN) || (BlockUtil.isBlockType(blockHit.getBlockPos(), Blocks.BEDROCK) && CrystalUtil.hasNoEntityOnIt(blockHit.getBlockPos())))) {
                    if (this.field139.getValue()) {
                        Mouse.pressKeyDefaultDelay(1);
                    }
                    TargetUtil.placeBlock(blockHit, true);
                    if (this.field138.getValue() && method402 <= this.field141.getValue() && CrystalUtil.hasNoEntityOnIt(blockHit.getBlockPos()) && blockHit.getSide() == Direction.UP) {
                        this.mc.particleManager.addBlockBreakingParticles(blockHit.getBlockPos(), blockHit.getSide());
                    }
                    this.field142 = this.field133.getValueInt();
                }
                if (this.field138.getValue()) {
                    if (!b2 && method402 <= this.field136.getValueInt()) {
                        if (BlockUtil.isBlockType(blockHit.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlockType(blockHit.getBlockPos(), Blocks.BEDROCK)) {
                            return;
                        }
                        if (this.field139.getValue()) {
                            if (BlockUtil.isBlockType(blockHit.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlockType(blockHit.getBlockPos(), Blocks.BEDROCK)) {
                                if (CrystalUtil.hasNoEntityOnIt(blockHit.getBlockPos())) {
                                    Mouse.pressKeyDefaultDelay(0);
                                }
                            } else {
                                Mouse.pressKeyDefaultDelay(0);
                            }
                        }
                        this.mc.interactionManager.attackBlock(blockHit.getBlockPos(), blockHit.getSide());
                        this.mc.player.swingHand(Hand.MAIN_HAND);
                        this.mc.particleManager.addBlockBreakingParticles(blockHit.getBlockPos(), blockHit.getSide());
                        this.mc.interactionManager.updateBlockBreakingProgress(blockHit.getBlockPos(), blockHit.getSide());
                        this.field143 = this.field134.getValueInt();
                    }
                    if (!b && method402 <= this.field135.getValueInt() && b2 && this.field139.getValue()) {
                        Mouse.pressKeyDefaultDelay(1);
                    }
                }
            }
            if (this.mc.crosshairTarget.getType() == HitResult.Type.MISS && this.field138.getValue()) {
                if (!b2 && method402 <= this.field136.getValueInt()) {
                    if (this.mc.interactionManager.hasLimitedAttackSpeed()) {
                        this.mc.attackCooldown = 10;
                    }
                    if (this.field139.getValue()) {
                        Mouse.pressKeyDefaultDelay(0);
                    }
                    this.mc.player.resetLastAttackedTicks();
                    this.mc.player.swingHand(Hand.MAIN_HAND);
                    this.field143 = this.field134.getValueInt();
                }
                if (!b && method402 <= this.field135.getValueInt() && b2 && this.field139.getValue()) {
                    Mouse.pressKeyDefaultDelay(1);
                }
            }
        }
        final int method403 = RandomUtil.getRandom(1, 100);
        final HitResult crosshairTarget2 = this.mc.crosshairTarget;
        if (crosshairTarget2 instanceof final EntityHitResult entityHitResult) {
            if (!b2 && method403 <= this.field136.getValueInt()) {
                final Entity entity = entityHitResult.getEntity();
                if (!this.field138.getValue() && !(entity instanceof EndCrystalEntity) && !(entity instanceof SlimeEntity)) {
                    return;
                }
                if (this.field139.getValue()) {
                    Mouse.pressKeyDefaultDelay(0);
                }
                TargetUtil.attack(entity, true);
                this.field143 = this.field134.getValueInt();
            }
        }
    }

    @Override
    public void onItemUse(final ItemUseEvent event) {
        if (this.mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
            final HitResult crosshairTarget = this.mc.crosshairTarget;
            if (crosshairTarget instanceof final BlockHitResult blockHitResult) {
                if (this.mc.crosshairTarget.getType() == HitResult.Type.BLOCK && (BlockUtil.isBlockType(blockHitResult.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlockType(blockHitResult.getBlockPos(), Blocks.BEDROCK))) {
                    event.cancelEvent();
                }
            }
        }
    }

    private boolean method122() {
        //if (this.mc.world.getPlayers().parallelStream().filter(this::method127).filter(this::method126)/*.filter(Class41::method125).filter(Class41::method124).anyMatch(Class41::method123)*/) {
        final LivingEntity getAttacking = this.mc.player.getAttacking();
        return !(getAttacking instanceof PlayerEntity playerEntity);
        //}
    }

    private boolean method126(final AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return abstractClientPlayerEntity.squaredDistanceTo(this.mc.player) < 36.0;
    }

    private boolean method127(final AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return abstractClientPlayerEntity != this.mc.player;
    }
}
