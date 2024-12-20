package com.ufo.cart.module.modules.render;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.NumberSetting;
import com.ufo.cart.utils.render.Render3D;
import com.ufo.cart.utils.render.ThemeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockESP extends Module implements Render3DListener {

    private final NumberSetting opacity = new NumberSetting("Opacity", 0.0, 255.0, 50, 1);
    private final NumberSetting range = new NumberSetting("Range", 10, 100, 25, 1);

    private final List<Block> oreBlocks = new ArrayList<>();
    private final Map<Block, Color> blockColors = new HashMap<>();

    public BlockESP() {
        super("Block ESP", "Renders blocks through walls.", 0, Category.RENDER);
        addSetting(opacity);
        addSetting(range);

        oreBlocks.add(Blocks.COAL_ORE);
        oreBlocks.add(Blocks.IRON_ORE);
        oreBlocks.add(Blocks.GOLD_ORE);
        oreBlocks.add(Blocks.DIAMOND_ORE);
        oreBlocks.add(Blocks.EMERALD_ORE);
        oreBlocks.add(Blocks.REDSTONE_ORE);
        oreBlocks.add(Blocks.LAPIS_ORE);
        oreBlocks.add(Blocks.NETHER_QUARTZ_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_COAL_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_IRON_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_GOLD_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_EMERALD_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_LAPIS_ORE);
        oreBlocks.add(Blocks.DEEPSLATE_COPPER_ORE);
        oreBlocks.add(Blocks.NETHER_GOLD_ORE);
        oreBlocks.add(Blocks.ANCIENT_DEBRIS);

        blockColors.put(Blocks.COAL_ORE, Color.black);
        blockColors.put(Blocks.IRON_ORE, new Color(186, 152, 128));
        blockColors.put(Blocks.GOLD_ORE, new Color(205, 152, 30));
        blockColors.put(Blocks.DIAMOND_ORE, new Color(80, 237, 220));
        blockColors.put(Blocks.EMERALD_ORE, new Color(4, 153, 40));
        blockColors.put(Blocks.REDSTONE_ORE, Color.red);
        blockColors.put(Blocks.LAPIS_ORE, new Color(40, 87, 181));
        blockColors.put(Blocks.NETHER_QUARTZ_ORE, Color.white);
        blockColors.put(Blocks.DEEPSLATE_COAL_ORE, Color.black);
        blockColors.put(Blocks.DEEPSLATE_IRON_ORE, new Color(186, 152, 128));
        blockColors.put(Blocks.DEEPSLATE_GOLD_ORE, new Color(205, 152, 30));
        blockColors.put(Blocks.DEEPSLATE_DIAMOND_ORE, new Color(80, 237, 220));
        blockColors.put(Blocks.DEEPSLATE_EMERALD_ORE, new Color(4, 153, 40));
        blockColors.put(Blocks.DEEPSLATE_REDSTONE_ORE, Color.red);
        blockColors.put(Blocks.DEEPSLATE_LAPIS_ORE, new Color(40, 87, 181));
        blockColors.put(Blocks.COPPER_ORE, new Color(229, 124, 86));
        blockColors.put(Blocks.DEEPSLATE_COPPER_ORE, new Color(229, 124, 86));
        blockColors.put(Blocks.ANCIENT_DEBRIS, new Color(59, 34, 28));
        blockColors.put(Blocks.NETHER_GOLD_ORE, new Color(205, 152, 30));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.eventBus.registerPriorityListener(Render3DListener.class, this);
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.eventBus.unregister(Render3DListener.class, this);
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onRender(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        MatrixStack matrices = event.matrices;
        Camera cam = mc.getBlockEntityRenderDispatcher().camera;

        if (cam == null) return;

        Vec3d camPos = cam.getPos();
        int renderDistance = (int) range.getValue();
        BlockPos playerPos = mc.player.getBlockPos();
        float fov = mc.options.getFov().getValue().intValue() + 45;

        Map<BlockPos, Color> blocksToRender = new HashMap<>();

        for (int x = playerPos.getX() - renderDistance; x <= playerPos.getX() + renderDistance; x++) {
            for (int y = Math.max(mc.world.getBottomY(), playerPos.getY() - renderDistance); y <= Math.min(mc.world.getTopY() - 1, playerPos.getY() + renderDistance); y++) {
                for (int z = playerPos.getZ() - renderDistance; z <= playerPos.getZ() + renderDistance; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    if (isInFrustum(pos, cam, fov)) {
                        Block block = mc.world.getBlockState(pos).getBlock();
                        if (oreBlocks.contains(block)) {
                            blocksToRender.put(pos, blockColors.getOrDefault(block, ThemeUtils.getMainColor()));
                        }
                    }
                }
            }
        }

        blocksToRender.forEach((pos, color) -> {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cam.getPitch()));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cam.getYaw() + 180.0F));
            matrices.translate(pos.getX() - camPos.x, pos.getY() - camPos.y, pos.getZ() - camPos.z);

            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pos.offset(dir);
                if (mc.world.getBlockState(neighborPos).isAir() || !mc.world.getBlockState(neighborPos).isOpaqueFullCube(mc.world, neighborPos)) {
                    Box faceBox = getFaceBox(dir);
                    Render3D.render3DBox(matrices, faceBox, color, opacity.getValueInt(), 1f);
                }
            }
            matrices.pop();
        });
    }

    private boolean isInFrustum(BlockPos pos, Camera cam, float fov) {
        Vec3d camPos = cam.getPos();
        Vec3d blockPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        Vec3d lookVec = Vec3d.fromPolar(cam.getPitch(), cam.getYaw());
        Vec3d toBlock = blockPos.subtract(camPos);

        double dotProduct = toBlock.normalize().dotProduct(lookVec);

        double angle = Math.toDegrees(Math.acos(dotProduct));

        return angle < fov / 2.0;
    }

    private Box getFaceBox(Direction dir) {
        double minX = 0, minY = 0, minZ = 0;
        double maxX = 1, maxY = 1, maxZ = 1;

        switch (dir) {
            case DOWN:
                maxY = 0.01;
                break;
            case UP:
                minY = 0.99;
                break;
            case NORTH:
                maxZ = 0.01;
                break;
            case SOUTH:
                minZ = 0.99;
                break;
            case WEST:
                maxX = 0.01;
                break;
            case EAST:
                minX = 0.99;
                break;
        }
        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }
}