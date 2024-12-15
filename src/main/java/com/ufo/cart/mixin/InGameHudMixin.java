package com.ufo.cart.mixin;

import com.ufo.cart.Client;
import com.ufo.cart.event.events.Render2DEvent;
import com.ufo.cart.gui.Hud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.hud.InGameHud")
public class InGameHudMixin {
    @Inject(at = @At("HEAD"), method = "renderPlayerList(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", cancellable = true)
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
            Hud.renderArrayList(tickCounter, context, new MatrixStack());
    }
}
