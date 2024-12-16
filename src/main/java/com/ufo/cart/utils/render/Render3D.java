package com.ufo.cart.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ufo.cart.Client;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import java.awt.*;

import static com.ufo.cart.Client.mc;

public class Render3D {
    public static void render3DBox(MatrixStack matrixStack, Box box, Color color, float lineThickness) {
        Camera camera = mc.gameRenderer.getCamera();
        MatrixStack.Entry matrixEntry = matrixStack.peek();

        color = Color.white;

        double x1 = box.minX;
        double y1 = box.minY;
        double z1 = box.minZ;
        double x2 = box.maxX;
        double y2 = box.maxY;
        double z2 = box.maxZ;

        x1 -= camera.getPos().x;
        y1 -= camera.getPos().y;
        z1 -= camera.getPos().z;
        x2 -= camera.getPos().x;
        y2 -= camera.getPos().y;
        z2 -= camera.getPos().z;

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.lineWidth(lineThickness);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y1, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y1, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y1, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y1, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y1, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y1, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y1, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y1, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y2, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y2, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y2, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y2, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y2, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y2, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y2, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y2, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y1, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y2, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y1, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y2, (float) z1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y1, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x2, (float) y2, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y1, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        bufferBuilder.vertex(matrixEntry.getPositionMatrix(), (float) x1, (float) y2, (float) z2).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        BufferRenderer.draw(bufferBuilder.end());
    }
}