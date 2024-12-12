package com.ufo.cart.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import com.ufo.cart.Client;
import com.ufo.cart.utils.Utils;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public final class RenderUtils implements Utils {
    public static VertexSorter vertexSorter;
    public static boolean rendering3D = true;

    public static void unscaledProjection() {
        vertexSorter = RenderSystem.getVertexSorting();
        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, Client.mc.getWindow().getFramebufferWidth(), Client.mc.getWindow().getFramebufferHeight(), 0, 1000, 21000), VertexSorter.BY_Z);
        rendering3D = false;
    }

    public static void scaledProjection() {
        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0, (float) (Client.mc.getWindow().getFramebufferWidth() / Client.mc.getWindow().getScaleFactor()), (float) (Client.mc.getWindow().getFramebufferHeight() / Client.mc.getWindow().getScaleFactor()), 0, 1000, 21000), vertexSorter);
        rendering3D = true;
    }

    public static void renderFilledBox(MatrixStack matrices, float f, float f2, float f3, float f4, float f5, float f6, Color color) {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f2, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f3);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f6);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), f4, f5, f6);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}