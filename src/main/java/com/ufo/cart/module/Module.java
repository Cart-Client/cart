package com.ufo.cart.module;

import com.ufo.cart.Client;
import com.ufo.cart.event.EventBus;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.module.setting.Setting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module implements EventListener {
    @Getter
    private final List<Setting> settings = new ArrayList<>();
    protected static MinecraftClient mc = MinecraftClient.getInstance();

    @Setter
    @Getter
    private String name;

    @Getter
    @Setter
    private String suffix;

    @Setter
    @Getter
    private String description;

    @Getter
    private boolean enabled;

    @Setter
    @Getter
    private int key;

    @Setter
    @Getter
    private Category category;

    protected final EventBus eventBus;

    public Module(String name, String description, int key, Category category) {
        this.name = name;
        this.description = description;
        this.enabled = false;
        this.key = key;
        this.category = category;
        this.eventBus = Client.INSTANCE.getEVENT_BUS();
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public void addSetting(Setting setting) {
        this.settings.add(setting);
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    protected void onEnable() {
        eventBus.register(this.getClass(), this, 0);
        System.out.println(this.name + " enabled.");
    }

    protected void onDisable() {
        eventBus.unregister(this.getClass(), this);
        System.out.println(this.name + " disabled.");
    }
}
