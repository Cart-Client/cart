package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.events.ItemUseEvent;
import com.ufo.cart.event.listeners.ItemUseListener;
import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.utils.other.BlockUtil;
import com.ufo.cart.utils.other.CrystalUtil;
import com.ufo.cart.utils.math.RandomUtil;
import com.ufo.cart.utils.other.Mouse;
import com.ufo.cart.utils.math.TargetUtil;
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
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting breakDelay = new NumberSetting("Break Delay", 0.0, 20.0, 0.0, 1.0);
    private final NumberSetting placeChance = new NumberSetting("Place Chance", 0.0, 100.0, 100.0, 1.0);
    private final NumberSetting breakChance = new NumberSetting("Break Chance", 0.0, 100.0, 100.0, 1.0);
    private final BooleanSetting stopOnKill = new BooleanSetting("Stop on Kill", false);
    private final BooleanSetting fakePunch = new BooleanSetting("Fake Punch", false);
    private final BooleanSetting clickSimulation = new BooleanSetting("Click Simulation", false);
    private final BooleanSetting damageTick = new BooleanSetting("Damage Tick", false);
    private final NumberSetting particleChance = new NumberSetting("Particle Chance", 0.0, 100.0, 20.0, 1.0);

    private int placeTimer = 0;
    private int breakTimer = 0;

    public AutoCrystal() {
        super("Auto Crystal", "Automatically crystals fast for you", 0, Category.COMBAT);
        this.addSettings(new Setting[]{
                placeDelay, breakDelay, placeChance, breakChance,
                stopOnKill, fakePunch, clickSimulation, damageTick, particleChance
        });
    }

    private static boolean isRecentlyDamaged(AbstractClientPlayerEntity player) {
        return player.hurtTime >= 2;
    }

    private static boolean isNotOnGround(AbstractClientPlayerEntity player) {
        return !player.isOnGround();
    }

    private static boolean hasNoAttacker(AbstractClientPlayerEntity player) {
        return player.getLastAttacker() == null;
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        this.eventBus.registerPriorityListener(ItemUseListener.class, this);
        placeTimer = 0;
        breakTimer = 0;
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
        if (mc.currentScreen != null || mc.player.isUsingItem() || mc.crosshairTarget == null) {
            return;
        }

        if (stopOnKill.getValue() && TargetUtil.isDeadNearby()) {
            return;
        }

        if (damageTick.getValue() && isPlayerUnderAttack()) {
            return;
        }

        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 1) != 1) {
            return;
        }

        if (mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL) {
            return;
        }

        boolean canPlace = placeTimer == 0;
        boolean canBreak = breakTimer == 0;

        int chance = RandomUtil.getRandom(1, 100);

        // Check for BlockHitResult
        if (canPlace && chance <= placeChance.getValueInt() && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            handlePlaceCrystal((BlockHitResult) mc.crosshairTarget);
        }

        // Check for EntityHitResult
        if (canBreak && chance <= breakChance.getValueInt() && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            handleBreakCrystal((EntityHitResult) mc.crosshairTarget);
        }

        if (placeTimer > 0) {
            placeTimer--;
        }
        if (breakTimer > 0) {
            breakTimer--;
        }
    }

    private void handlePlaceCrystal(BlockHitResult hitResult) {
        if (hitResult.getType() != HitResult.Type.BLOCK) return;

        if ((BlockUtil.isBlockType(hitResult.getBlockPos(), Blocks.OBSIDIAN) ||
                BlockUtil.isBlockType(hitResult.getBlockPos(), Blocks.BEDROCK)) &&
                CrystalUtil.hasNoEntityOnIt(hitResult.getBlockPos())) {

            if (clickSimulation.getValue()) {
                Mouse.pressKeyDefaultDelay(1);
            }

            TargetUtil.placeBlock(hitResult, true);

            if (fakePunch.getValue() && RandomUtil.getRandom(1, 100) <= particleChance.getValue() &&
                    hitResult.getSide() == Direction.UP) {
                mc.particleManager.addBlockBreakingParticles(hitResult.getBlockPos(), hitResult.getSide());
            }
            placeTimer = placeDelay.getValueInt();
        }
    }

    private void handleBreakCrystal(EntityHitResult hitResult) {
        if (!(hitResult.getEntity() instanceof EndCrystalEntity || hitResult.getEntity() instanceof SlimeEntity)) return;

        if (clickSimulation.getValue()) {
            Mouse.pressKeyDefaultDelay(0);
        }

        TargetUtil.attack(hitResult.getEntity(), true);
        breakTimer = breakDelay.getValueInt();
    }

    private boolean isPlayerUnderAttack() {
        LivingEntity attacker = mc.player.getAttacking();
        return !(attacker instanceof PlayerEntity);
    }

    @Override
    public void onItemUse(final ItemUseEvent event) {
        if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL &&
                mc.crosshairTarget instanceof BlockHitResult hitResult &&
                (BlockUtil.isBlockType(hitResult.getBlockPos(), Blocks.OBSIDIAN) ||
                        BlockUtil.isBlockType(hitResult.getBlockPos(), Blocks.BEDROCK))) {
            event.cancelEvent();
        }
    }
}
