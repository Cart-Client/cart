package com.ufo.cart;

import com.ufo.cart.event.EventBus;
import com.ufo.cart.module.ModuleManager;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

@Getter
public final class Client {
    public static MinecraftClient mc;
    public static Client INSTANCE;
    public EventBus EVENT_BUS;
    private final ModuleManager moduleManager;


    public Client() {
        INSTANCE = this;
        mc = MinecraftClient.getInstance();
        this.EVENT_BUS = new EventBus();
        moduleManager = new ModuleManager();
    }


    public static Client getInstance() {
        return INSTANCE;
    }
}
