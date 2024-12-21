package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.events.ItemUseEvent;
import com.ufo.cart.event.listeners.ItemUseListener;
import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.utils.math.MathUtils;
import com.ufo.cart.utils.other.InventoryUtil;
import com.ufo.cart.utils.other.MouseSimulation;
import com.ufo.cart.utils.other.BlockUtil;
import com.ufo.cart.utils.other.CrystalUtils;
import com.ufo.cart.utils.world.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class cw extends Module implements TickListener, ItemUseListener {

    private final NumberSetting placeInterval = new NumberSetting("Place Interval", 0, 20, 0, 1);
    private final NumberSetting breakInterval = new NumberSetting("Break Interval", 0, 20, 0, 1);
    private final NumberSetting placeSuccessRate = new NumberSetting("Place Rate", 0, 100, 100, 1);
    private final NumberSetting breakSuccessRate = new NumberSetting("Break Rate", 0, 100, 100, 1);
    private final BooleanSetting haltOnKill = new BooleanSetting("Halt on Kill", false);
    private final BooleanSetting simulateClicks = new BooleanSetting("Emulate Clicks", false);
    private final BooleanSetting damageCheck = new BooleanSetting("Damage Tick", false);
    private final BooleanSetting ignoreWeakness = new BooleanSetting("Ignore Weakness", false);
    private final BooleanSetting fakeHandSwing = new BooleanSetting("Fake Punch", false);
    private final NumberSetting particleProbability = new NumberSetting("Particle Chance", 0, 100, 20, 1);


    private int placeTimer;
    private int breakTimer;
    public boolean isCrystalling;

    public cw() {
        super("cw", "fast crystals buzzo", -1, Category.COMBAT);
        addSettings(placeInterval, breakInterval, placeSuccessRate, breakSuccessRate, haltOnKill, fakeHandSwing, simulateClicks, damageCheck, ignoreWeakness, particleProbability);
    }


    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        this.eventBus.registerPriorityListener(ItemUseListener.class, this);
        placeTimer = 0;
        breakTimer = 0;
        isCrystalling = false;
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
        if (mc.currentScreen != null)
            return;

        boolean shouldDelayPlace = (placeTimer != 0);
        boolean shouldDelayBreak = (breakTimer != 0);

        if (haltOnKill.getValue() && WorldUtils.isDeadBodyNearby())
            return;

        int randomValue = MathUtils.randomInt(1, 100);

        if (shouldDelayPlace)
            placeTimer--;

        if (shouldDelayBreak)
            breakTimer--;

        if (mc.player.isUsingItem())
            return;

        if (damageCheck.getValue() && checkDamageTick())
            return;

        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 1) != 1) {
            return;
        }


        isCrystalling = true;

        if (mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL)
            return;


        if (mc.crosshairTarget instanceof BlockHitResult blockHit) {
            if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                if (!shouldDelayPlace && randomValue <= placeSuccessRate.getValueInt()) {
                    if ((BlockUtil.isBlock(blockHit.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlock(blockHit.getBlockPos(), Blocks.BEDROCK))
                            && CrystalUtils.canPlaceCrystalClientAssumeObsidian(blockHit.getBlockPos())) {

                        if (simulateClicks.getValue())
                            MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

                        WorldUtils.placeBlock(blockHit, true);

                        if (fakeHandSwing.getValue()) {
                            if (randomValue <= particleProbability.getValue())
                                if (CrystalUtils.canPlaceCrystalClientAssumeObsidian(blockHit.getBlockPos()) && blockHit.getSide() == Direction.UP)
                                    mc.particleManager.addBlockBreakingParticles(blockHit.getBlockPos(), blockHit.getSide());
                        }

                        placeTimer = placeInterval.getValueInt();
                    }
                }

                if (fakeHandSwing.getValue()) {
                    if (!shouldDelayBreak && randomValue <= breakSuccessRate.getValueInt()) {

                        if (BlockUtil.isBlock(blockHit.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlock(blockHit.getBlockPos(), Blocks.BEDROCK))
                            return;

                        if (simulateClicks.getValue()) {
                            if (BlockUtil.isBlock(blockHit.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlock(blockHit.getBlockPos(), Blocks.BEDROCK)) {
                                if (CrystalUtils.canPlaceCrystalClientAssumeObsidian(blockHit.getBlockPos()))
                                    MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);
                            } else MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);
                        }

                        mc.interactionManager.attackBlock(blockHit.getBlockPos(), blockHit.getSide());
                        mc.player.swingHand(Hand.MAIN_HAND);
                        mc.particleManager.addBlockBreakingParticles(blockHit.getBlockPos(), blockHit.getSide());
                        mc.interactionManager.updateBlockBreakingProgress(blockHit.getBlockPos(), blockHit.getSide());

                        breakTimer = breakInterval.getValueInt();
                    }

                    if (!shouldDelayPlace && randomValue <= placeSuccessRate.getValueInt() && shouldDelayBreak) {
                        if (simulateClicks.getValue())
                            MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
                    }
                }

            }
            if (mc.crosshairTarget.getType() == HitResult.Type.MISS) {
                if (fakeHandSwing.getValue()) {
                    if (!shouldDelayBreak && randomValue <= breakSuccessRate.getValueInt()) {
                        if (mc.interactionManager.hasLimitedAttackSpeed())
                            mc.attackCooldown = 10;

                        if (simulateClicks.getValue())
                            MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);

                        mc.player.resetLastAttackedTicks();
                        mc.player.swingHand(Hand.MAIN_HAND);


                        breakTimer = breakInterval.getValueInt();
                    }

                    if (!shouldDelayPlace && randomValue <= placeSuccessRate.getValueInt() && shouldDelayBreak) {
                        if (simulateClicks.getValue())
                            MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
                    }
                }
            }

        }


        randomValue = MathUtils.randomInt(1, 100);


        if (mc.crosshairTarget instanceof EntityHitResult entityHit) {
            if (!shouldDelayBreak && randomValue <= breakSuccessRate.getValueInt()) {
                Entity entity = entityHit.getEntity();

                if (!fakeHandSwing.getValue() && !(entity instanceof EndCrystalEntity || entity instanceof SlimeEntity))
                    return;

                int prevSlot = mc.player.getInventory().selectedSlot;


                if (entity instanceof EndCrystalEntity || entity instanceof SlimeEntity)
                    if (ignoreWeakness.getValue() && checkWeakness())
                        InventoryUtil.selectSword();


                if (simulateClicks.getValue())
                    MouseSimulation.mouseClick(GLFW.GLFW_MOUSE_BUTTON_LEFT);

                WorldUtils.hitEntity(entity, true);
                breakTimer = breakInterval.getValueInt();

                if (ignoreWeakness.getValue())
                    InventoryUtil.setInvSlot(prevSlot);
            }
        }
    }

    @Override
    public void onItemUse(ItemUseEvent event) {
        if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
            if ((mc.crosshairTarget instanceof BlockHitResult hit
                    && mc.crosshairTarget.getType() == HitResult.Type.BLOCK
                    && (BlockUtil.isBlock(hit.getBlockPos(), Blocks.OBSIDIAN) || BlockUtil.isBlock(hit.getBlockPos(), Blocks.BEDROCK)))) {
                event.cancelEvent();
            }
        }
    }

    private boolean checkWeakness() {
        assert mc.player != null;
        StatusEffectInstance weakness = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
        StatusEffectInstance strength = mc.player.getStatusEffect(StatusEffects.STRENGTH);
        return !(weakness == null || strength != null && strength.getAmplifier() > weakness.getAmplifier() || WorldUtils.isTool(mc.player.getMainHandStack()));
    }

    private boolean checkDamageTick() {
        return mc.world.getPlayers().parallelStream()
                .filter(e -> e != mc.player)
                .filter(e -> e.squaredDistanceTo(mc.player) < 36)
                .filter(e -> e.getLastAttacker() == null)
                .filter(e -> !e.isOnGround())
                .anyMatch(e -> e.hurtTime >= 2)
                && !(mc.player.getAttacking() instanceof PlayerEntity);
    }
}