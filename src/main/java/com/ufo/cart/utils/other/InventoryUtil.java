package com.ufo.cart.utils.other;

import com.ufo.cart.Client;
import com.ufo.cart.mixin.ClientPlayerInteractionManagerAccessor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class InventoryUtil {
    static final boolean field462;

    static {
        field462 = !InventoryUtil.class.desiredAssertionStatus();
    }

    public static void setSlot(final int slot) {
        Client.mc.player.getInventory().selectedSlot = slot;
        ((ClientPlayerInteractionManagerAccessor) Client.mc.interactionManager).syncSlot();
    }

    public static boolean method308(final Predicate<Item> item) {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (item.test(getInventory.getStack(i).getItem())) {
                getInventory.selectedSlot = i;
                return true;
            }
        }
        return false;
    }

    public static boolean method309(final Item item) {
        return method308(a -> InventoryUtil.equals(item, (Item) a));
    }

    public static boolean method310(final Predicate<Item> item) {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            final ItemStack getAbilities = getInventory.getStack(i);
            if (item.test(getAbilities.getItem())) {
                return true;
            }
        }
        return false;
    }

    public static int method311(final Predicate<Item> item) {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        int n = 0;
        for (int i = 0; i < 36; i++) {
            final ItemStack getAbilities = getInventory.getStack(i);
            if (item.test(getAbilities.getItem())) {
                n += getAbilities.getCount();
            }
        }
        return n;
    }

    public static int method312(final Predicate<Item> item) {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        int n = 0;
        for (int i = 9; i < 36; i++) {
            final ItemStack getAbilities = getInventory.getStack(i);
            if (item.test(getAbilities.getItem())) {
                n += getAbilities.getCount();
            }
        }
        return n;
    }

    public static int method315() {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        for (int i = 9; i < 36; i++) {
            if (getInventory.main.get(i).getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }

    public static boolean holdAxe() {
        final int axeSlot = getAxe();
        if (axeSlot != -1) {
            Client.mc.player.getInventory().selectedSlot = axeSlot;
            return true;
        }
        return false;
    }

    public static int method317() {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        final Random random = new Random();
        final int n3 = random.nextInt(27) + 9;
        for (int i = 0; i < 27; i++) {
            int n4 = (n3 + i) % 36;
            final ItemStack itemStack = getInventory.main.get(n4);
            if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                return n4;
            }
        }
        return -1;
    }

    public static int getAxe() {
        final PlayerInventory getInventory = Client.mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (((Inventory) getInventory).getStack(i).getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }

    private static boolean equals(final Item item, final Item item2) {
        return item.equals(item2);
    }
}
