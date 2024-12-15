package com.ufo.cart.mixin;

import com.ufo.cart.Client;
import com.ufo.cart.module.ModuleManager;
import com.ufo.cart.module.modules.combat.Reach;
import com.ufo.cart.utils.other.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

import static com.ufo.cart.Client.mc;

@Mixin (PlayerEntity.class)
public abstract class ReachMixin {
    @Inject(at = @At("HEAD"), method = "getEntityInteractionRange()D", cancellable = true)
    private void getEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
        if (Objects.requireNonNull(Client.getInstance().getModuleManager().getModule(Reach.class)).isEnabled()) {
            Double reach = (Objects.requireNonNull(Client.getInstance().getModuleManager().getModule(Reach.class))).reach;
            assert mc.player != null;
            if (PlayerUtil.findClosest(mc.player, reach) != null)
                cir.setReturnValue(reach);
        }
    }
}
