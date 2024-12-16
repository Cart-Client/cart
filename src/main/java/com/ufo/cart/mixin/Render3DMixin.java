package com.ufo.cart.mixin;

import com.ufo.cart.Client;
import com.ufo.cart.event.events.Render3DEvent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.ufo.cart.Client.mc;

@Mixin(GameRenderer.class)
public class Render3DMixin {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void onRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (mc.world == null || mc.player == null) {
            return;
        }

        MatrixStack matrixStack = new MatrixStack();
        float tickDelta = 0;
        Render3DEvent render3DEvent = new Render3DEvent(matrixStack, tickDelta);
        Client.getInstance().getEVENT_BUS().callListeners(render3DEvent);
    }
}