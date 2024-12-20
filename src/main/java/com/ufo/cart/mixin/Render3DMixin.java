package com.ufo.cart.mixin;

import com.ufo.cart.Client;
import com.ufo.cart.event.events.Render3DEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class Render3DMixin {
    @Shadow public abstract Matrix4f getBasicProjectionMatrix(double fov);

    @Shadow protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);

    @Shadow @Final private Camera camera;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void onWorldRender(RenderTickCounter tickCounter, CallbackInfo ci) {
        double d = getFov(camera, tickCounter.getTickDelta(true), true);
        Matrix4f matrix4f = getBasicProjectionMatrix(d);
        MatrixStack matrixStack = new MatrixStack();
        float tickDelta = 0;
        Render3DEvent render3DEvent = new Render3DEvent(matrixStack, tickDelta, matrix4f);
        Client.getInstance().getEVENT_BUS().callListeners(render3DEvent);
    }
}