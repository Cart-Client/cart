package com.ufo.cart.mixin;

import com.ufo.cart.Client;
import com.ufo.cart.module.modules.render.Chams;
import com.ufo.cart.module.modules.render.FullBright;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static org.lwjgl.opengl.GL11C.*;

@Mixin(net.minecraft.client.render.WorldRenderer.class)
public class WorldRenderer {

    @Unique
    private boolean isRenderingChams = false;

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void injectChamsForEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (Objects.requireNonNull(Client.INSTANCE.getModuleManager().getModule(Chams.class)).isEnabled() && entity instanceof PlayerEntity) {
            glEnable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1f, -1000000000000f);
            this.isRenderingChams = true;
        }
    }

    @Inject(method = "renderEntity", at = @At("RETURN"))
    private void injectChamsForEntityPost(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (Objects.requireNonNull(Client.INSTANCE.getModuleManager().getModule(Chams.class)).isEnabled() && entity instanceof PlayerEntity) {
            glPolygonOffset(1f, 1000000000000f);
            glDisable(GL_POLYGON_OFFSET_FILL);
            this.isRenderingChams = false;
        }
    }
}