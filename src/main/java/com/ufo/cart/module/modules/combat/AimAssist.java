package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.BooleanSetting;
import com.ufo.cart.module.setting.NumberSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import com.ufo.cart.utils.other.PlayerUtil;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import static com.ufo.cart.utils.math.RandomUtil.random;

public class AimAssist extends Module implements Render3DListener {

    private final NumberSetting FOV = new NumberSetting("FOV", 0.0, 180.0, 180.0, 1.0);
    private final NumberSetting Range = new NumberSetting("Range", 0.0, 10.0, 3.0, 1.0);
    private final NumberSetting Smoothness = new NumberSetting("Smoothness", 0.0, 10.0, 5.0, 0.1);
    private final NumberSetting Strength = new NumberSetting("Strength", 0.0, 1.0, 0.5, 0.01);
    private final BooleanSetting swordOnly = new BooleanSetting("Sword Only", false);
    private final BooleanSetting teamCheck = new BooleanSetting("Team Check", false);
    private final BooleanSetting mouseCheck = new BooleanSetting("Mouse Check", false);
    private final NumberSetting randomization = new NumberSetting("Chance", 0, 100, 50, 1);

    private double previousCursorX = 0;
    private double previousCursorY = 0;

    public AimAssist() {
        super("Aim Assist", "Automatically aims at targets", 0, Category.COMBAT);
        addSettings(FOV, Range, Smoothness, Strength, randomization, swordOnly, teamCheck, mouseCheck);
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

    private Vec3d getClosestPointOnBoundingBox(PlayerEntity target) {
        assert mc.player != null;
        Vec3d playerPos = mc.player.getPos().add(0, mc.player.getEyeHeight(mc.player.getPose()), 0);
        Box boundingBox = target.getBoundingBox();

        return new Vec3d(
                MathHelper.clamp(playerPos.x, boundingBox.minX, boundingBox.maxX),
                MathHelper.clamp(playerPos.y, boundingBox.minY, boundingBox.maxY),
                MathHelper.clamp(playerPos.z, boundingBox.minZ, boundingBox.maxZ)
        );
    }

    private boolean isMouseMoving() {
        double[] x = new double[1];
        double[] y = new double[1];
        GLFW.glfwGetCursorPos(mc.getWindow().getHandle(), x, y);
        boolean moving = x[0] != previousCursorX || y[0] != previousCursorY;
        previousCursorX = x[0];
        previousCursorY = y[0];
        return moving;
    }

    private double smoothStepLerp(double delta, double start, double end) {
        delta = MathHelper.clamp(delta, 0, 1);
        double t = delta * delta * (3 - 2 * delta);
        return start + MathHelper.wrapDegrees(end - start) * t;
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    @Override
    public void onRender(Render3DEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (mouseCheck.getValue() && !isMouseMoving()) return;

        if (swordOnly.getValue() && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) return;

        PlayerEntity target = PlayerUtil.findClosest(mc.player, Range.getValue());

        if (target == null || mc.player.distanceTo(target) > Range.getValue()) return;

        if (teamCheck.getValue() && target.getTeamColorValue() == mc.player.getTeamColorValue()) return;

        Vec3d closestPoint = getClosestPointOnBoundingBox(target);

        double diffX = closestPoint.x - mc.player.getX();
        double diffY = closestPoint.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = closestPoint.z - mc.player.getZ();

        double distance = MathHelper.sqrt((float) (diffX * diffX + diffZ * diffZ));

        float targetYaw = (float) (MathHelper.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        float targetPitch = (float) -(MathHelper.atan2(diffY, distance) * 180.0D / Math.PI);

        float yawDiff = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
        float pitchDiff = MathHelper.wrapDegrees(targetPitch - mc.player.getPitch());

        double angle = Math.toDegrees(Math.acos(MathHelper.clamp(Math.cos(Math.toRadians(yawDiff)) * Math.cos(Math.toRadians(pitchDiff)), -1.0, 1.0)));

        if (angle > FOV.getValue()) return;

        float smoothingFactor = (float) Math.pow(0.5, Smoothness.getValueFloat());
        float strengthFactor = Strength.getValueFloat();

        int gcd = gcd((int) Math.abs(yawDiff), (int) Math.abs(pitchDiff));
        if (gcd != 0) {
            yawDiff /= gcd;
        }


        if (random.nextInt(1, 100) <= randomization.getValueInt()) {
            mc.player.setYaw((float) smoothStepLerp(smoothingFactor * strengthFactor, mc.player.getYaw(), targetYaw));
        } else {
            mc.player.setYaw(mc.player.getYaw() + yawDiff * smoothingFactor * strengthFactor);
        }

        mc.player.setYaw(MathHelper.wrapDegrees(mc.player.getYaw()));
    }
}