package com.ufo.cart.module;

import com.ufo.cart.Client;
import com.ufo.cart.module.modules.client.ClickGUI;
import com.ufo.cart.module.modules.client.Theme;
import com.ufo.cart.module.modules.combat.AimAssist;
import com.ufo.cart.module.modules.combat.Reach;
import com.ufo.cart.module.modules.combat.TriggerBot;
import com.ufo.cart.module.modules.misc.CrystalOptimizer;
import com.ufo.cart.event.EventListener;
import com.ufo.cart.module.modules.player.AutoSprint;
import com.ufo.cart.module.modules.player.JumpReset;
import com.ufo.cart.module.modules.render.ESP;
import com.ufo.cart.module.modules.render.FullBright;
import com.ufo.cart.module.modules.render.HUD;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager implements EventListener {
    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {

        Client.INSTANCE.getEVENT_BUS().register(ModuleManager.class, this, 0);


        addModules();
    }

    public List<Module> getEnabledModules() {
        List<Module> enabled = new ArrayList<>();

        for (Module module : modules) {
            if (module.isEnabled()) {
                enabled.add(module);
            }
        }

        return enabled;
    }

    public List<Module> getModulesInCategory(Category category) {
        List<Module> categoryModules = new ArrayList<>();

        for (Module module : modules) {
            if (module.getCategory() == category) {
                categoryModules.add(module);
            }
        }

        return categoryModules;
    }

    public List<Module> getModules() {
        return modules;
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        for (Module module : modules) {
            if (moduleClass.isAssignableFrom(module.getClass())) {
                return (T) module;
            }
        }

        return null;
    }

    public void addModules() {

        add(new ClickGUI());
        add(new CrystalOptimizer());
        add(new Theme());
        add(new AutoSprint());
        add(new TriggerBot());
        add(new JumpReset());
        add(new AimAssist());
        add(new FullBright());
        add(new Reach());
        add(new HUD());
        add(new ESP());
    }

    public void add(Module module) {
        modules.add(module);
    }
}
