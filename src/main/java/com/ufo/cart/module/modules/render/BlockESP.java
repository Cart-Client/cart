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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BlockESP extends Module implements Render3DListener {

    private final NumberSetting opacity = new NumberSetting("Opacity", 0.0, 255.0, 50, 1);
    private final NumberSetting range = new NumberSetting("Range", 10, 100, 25, 1);

    private final List<Block> oreBlocks = new ArrayList<>();


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
        if (mc.world != null && mc.player != null) {
            MatrixStack matrices = event.matrices;
            Camera cam = mc.getBlockEntityRenderDispatcher().camera;

            if (cam == null) return;

            Vec3d camPos = cam.getPos();

            int renderDistance = (int) range.getValue();

            BlockPos playerPos = mc.player.getBlockPos();

            for (int x = playerPos.getX() - renderDistance; x <= playerPos.getX() + renderDistance; x++) {
                for (int y = Math.max(0, playerPos.getY() - renderDistance); y <= Math.min(mc.world.getHeight() - 1, playerPos.getY() + renderDistance); y++) {
                    for (int z = playerPos.getZ() - renderDistance; z <= playerPos.getZ() + renderDistance; z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        Block block = mc.world.getBlockState(pos).getBlock();

                        if (oreBlocks.contains(block)) {
                            Color color = ThemeUtils.getMainColor();

                            if (block == Blocks.COAL_ORE) {
                                color = Color.black;
                            } else if (block == Blocks.IRON_ORE) {
                                color = new Color(186, 152, 128);
                            } else if (block == Blocks.GOLD_ORE) {
                                color = new Color(205, 152, 30);
                            } else if (block == Blocks.DIAMOND_ORE) {
                                color = new Color(80, 237, 220);
                            } else if (block == Blocks.EMERALD_ORE) {
                                color = new Color(4, 153, 40);
                            } else if (block == Blocks.REDSTONE_ORE) {
                                color = Color.red;
                            } else if (block == Blocks.LAPIS_ORE) {
                                color = new Color(40, 87, 181);
                            } else if (block == Blocks.NETHER_QUARTZ_ORE) {
                                color = Color.white;
                            } else if (block == Blocks.DEEPSLATE_COAL_ORE) {
                                color = Color.black;
                            } else if (block == Blocks.DEEPSLATE_IRON_ORE) {
                                color = new Color(186, 152, 128);
                            } else if (block == Blocks.DEEPSLATE_GOLD_ORE) {
                                color = new Color(205, 152, 30);
                            } else if (block == Blocks.DEEPSLATE_DIAMOND_ORE) {
                                color = new Color(80, 237, 220);
                            } else if (block == Blocks.DEEPSLATE_EMERALD_ORE) {
                                color = new Color(4, 153, 40);
                            } else if (block == Blocks.DEEPSLATE_REDSTONE_ORE) {
                                color = Color.red;
                            } else if (block == Blocks.DEEPSLATE_LAPIS_ORE) {
                                color = new Color(40, 87, 181);
                            } else if (block == Blocks.COPPER_ORE) {
                                color = new Color(229, 124, 86);
                            } else if (block == Blocks.DEEPSLATE_COPPER_ORE) {
                                color = new Color(229, 124, 86);
                            } else if (block == Blocks.ANCIENT_DEBRIS) {
                                color = new Color(59, 34, 28);
                            } else if (block == Blocks.NETHER_GOLD_ORE) {
                                color = new Color(205, 152, 30);
                            }
                            matrices.push();

                            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cam.getPitch()));
                            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cam.getYaw() + 180.0F));


                            matrices.translate(pos.getX() - camPos.x, pos.getY() - camPos.y, pos.getZ() - camPos.z);

                            for (Direction dir : Direction.values()) {
                                BlockPos neighborPos = pos.offset(dir);
                                if (mc.world.getBlockState(neighborPos).isAir()) {
                                    Box faceBox = getFaceBox(dir);
                                    Render3D.render3DBox(matrices, faceBox, color, opacity.getValueInt(), 1f);
                                }
                            }

                            matrices.pop();
                        }
                    }
                }
            }
        }
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