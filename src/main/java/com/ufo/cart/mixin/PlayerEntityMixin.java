package com.ufo.cart.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ufo.cart.Client;
import com.ufo.cart.module.modules.combat.Reach;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

//	@Shadow
//	private PlayerInventory inventory;
//
//	@Shadow
//	public abstract boolean isSpectator();

//	@Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
//	public void onGetBlockBreakingSpeed(BlockState blockState, CallbackInfoReturnable<Float> ci) {
//		Client cart = Client.getInstance();
//		FastBreak fastBreak = (FastBreak) aoba.moduleManager.fastbreak;
//
//		// If fast break is enabled.
//		if (fastBreak.state.getValue()) {
//			// Multiply the break speed and override the return value.
//			float speed = inventory.getBlockBreakingSpeed(blockState);
//			speed *= fastBreak.multiplier.getValue();
//
//			if (!fastBreak.ignoreWater.getValue()) {
//				if (isSubmergedIn(FluidTags.WATER) || isSubmergedIn(FluidTags.LAVA) || !isOnGround()) {
//					speed /= 5.0F;
//				}
//			}
//
//			ci.setReturnValue(speed);
//		}
//	}
//
//	@Inject(at = { @At("HEAD") }, method = "getOffGroundSpeed()F", cancellable = true)
//	protected void onGetOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
//		return;
//	}

	@Inject(at = { @At("HEAD") }, method = "getBlockInteractionRange()D", cancellable = true)
	private void onBlockInteractionRange(CallbackInfoReturnable<Double> cir) {
		if (Client.getInstance().getModuleManager().reach.isEnabled()) {
			Reach reach = (Reach) Client.getInstance().getModuleManager().reach;
			cir.setReturnValue((double) reach.getReach());
		}
	}

	@Inject(at = { @At("HEAD") }, method = "getEntityInteractionRange()D", cancellable = true)
	private void onEntityInteractionRange(CallbackInfoReturnable<Double> cir) {
		if (Client.getInstance().getModuleManager().reach.isEnabled()) {
			Reach reach = (Reach) Client.getInstance().getModuleManager().reach;
			cir.setReturnValue((double) reach.getReach());
		}
	}
}
