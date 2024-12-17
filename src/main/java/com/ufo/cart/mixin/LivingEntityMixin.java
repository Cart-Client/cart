package com.ufo.cart.mixin;

import com.ufo.cart.event.listeners.TickListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements TickListener {
    @Inject(method = "tick", at = @At("TAIL"))
    void onTick(CallbackInfo callbackInfo) {
        if ((Object) this instanceof PlayerEntity) {
            ((LivingEntity) (Object) this).setGlowing(true);
        }
    }
}
