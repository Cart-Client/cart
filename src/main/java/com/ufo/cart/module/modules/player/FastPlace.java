package com.ufo.cart.module.modules.player;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.mixin.interfaces.MinecraftClientInterface;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;

public class FastPlace extends Module implements TickListener {

    MinecraftClientInterface MCI = (MinecraftClientInterface) MinecraftClient.getInstance();

    public FastPlace() {
        super("Fast Place", "Allows you to place blocks faster", 0, Category.PLAYER);
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        MCI.setItemUseCooldown(4);
        this.eventBus.unregister(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {

        assert mc.player != null;
        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
            MCI.setItemUseCooldown(4);
        }

        MCI.setItemUseCooldown(0);
    }
}
