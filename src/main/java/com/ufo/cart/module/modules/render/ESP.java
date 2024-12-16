package com.ufo.cart.module.modules.render;

import com.ufo.cart.event.events.Render3DEvent;
import com.ufo.cart.event.listeners.Render3DListener;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.utils.render.Render3D;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import java.awt.*;

public class ESP extends Module implements Render3DListener {

    public ESP() {
        super("ESP", "Show enemies through walls.", 0, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.eventBus.registerPriorityListener(Render3DListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.eventBus.unregister(Render3DListener.class, this);
    }

    @Override
    public void onRender(Render3DEvent event) {
        assert mc.world != null;
        for (Entity entity : mc.world.getPlayers()) {
            Render3D.render3DBox(new MatrixStack(), entity.getBoundingBox(), Color.white, 3);
            System.out.println("Obamacare");
        }
    }
}
