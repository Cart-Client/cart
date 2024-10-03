package com.ufo.cart.module.modules.combat;

import com.ufo.cart.event.listeners.TickListener;
import com.ufo.cart.mixin.HandledScreenMixin;
import com.ufo.cart.module.Category;
import com.ufo.cart.module.Module;
import com.ufo.cart.module.setting.Setting;
import com.ufo.cart.module.setting.NumberSetting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public final class HoverTotem extends Module implements TickListener {
    private final NumberSetting delaySetting;
    private final NumberSetting totemSlotSetting;
    private int delayCounter;

    public HoverTotem() {
        super("Hover Totem", "Equips a totem in your totem and offhand slots if a totem is hovered", 0, Category.COMBAT);
        this.delaySetting = new NumberSetting("Delay", 0.0, 20.0, 0.0, 1.0);
        this.totemSlotSetting = new NumberSetting("Totem Slot", 1.0, 9.0, 1.0, 1.0);
        this.addSettings(new Setting[]{this.delaySetting, this.totemSlotSetting});
    }

    @Override
    public void onEnable() {
        this.eventBus.registerPriorityListener(TickListener.class, this);
        this.delayCounter = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.eventBus.unregister(TickListener.class, this);
        super.onDisable();
    }

    @Override
    public void onTick() {
        Screen currentScreen = this.mc.currentScreen;
        InventoryScreen inventoryScreen = (InventoryScreen) currentScreen;

        if (!(inventoryScreen instanceof InventoryScreen)) this.delayCounter = this.delaySetting.getValueInt();
        else {
            Slot focusedSlot = ((HandledScreenMixin) inventoryScreen).getFocusedSlot();
            if (focusedSlot != null) {
                int index = focusedSlot.getIndex();
                int totemStartIndex = 35;

                if (index > totemStartIndex) return;

                index = totemSlotSetting.getValueInt();
                int totemIndex = index - totemStartIndex;
                Item item = focusedSlot.getStack().getItem();
                ItemStack totemStack = this.mc.player.getInventory().getStack(totemIndex);

                if (item == Items.TOTEM_OF_UNDYING) {
                    if (this.delayCounter > 0) {
                        --this.delayCounter;
                        return;
                    }
                    this.mc.interactionManager.clickSlot(inventoryScreen.getScreenHandler().syncId, index, totemIndex, SlotActionType.SWAP, this.mc.player);
                    this.delayCounter = this.delaySetting.getValueInt();
                } else {
                    this.mc.player.getOffHandStack();
                    if (totemStack.isOf(Items.TOTEM_OF_UNDYING)) return;
                    if (this.delayCounter > 0) {
                        --this.delayCounter;
                        return;
                    }
                    this.mc.interactionManager.clickSlot(inventoryScreen.getScreenHandler().syncId, index, 40, SlotActionType.SWAP, this.mc.player);
                    this.delayCounter = this.delaySetting.getValueInt();
                }
            }
        }
    }
}
