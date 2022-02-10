package me.ci.folks.npc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class NPCInventory implements IInventory {

    public static final int HEAD_SLOT = 0;
    public static final int BODY_SLOT = 1;
    public static final int LEGS_SLOT = 2;
    public static final int FEET_SLOT = 3;

    private final NonNullList<ItemStack> items;
    private final NPCEntity npc;
    private final int storageSlots;
    private final int armorSlots;
    private int selected = 0;

    public NPCInventory(NPCEntity npc, int storageSlots, int armorSlots) {
        if (npc == null)
            throw new IllegalArgumentException("NPC cannot be null!");

        if (storageSlots < 1)
            throw new IllegalArgumentException("Number of storage slots must be at least 1!");

        if (armorSlots < 4)
            throw new IllegalArgumentException("Number of armor slots must be at least 4!");

        this.npc = npc;
        this.storageSlots = storageSlots;
        this.armorSlots = armorSlots;

        int totalSize = storageSlots + armorSlots + 1;
        items = NonNullList.withSize(totalSize, ItemStack.EMPTY);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0 || slot >= items.size())
            return ItemStack.EMPTY;

        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        if (slot < 0 || slot >= items.size())
            return ItemStack.EMPTY;

        if (items.get(slot).isEmpty())
            return ItemStack.EMPTY;

        return ItemStackHelper.removeItem(items, slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot < 0 || slot >= items.size())
            return ItemStack.EMPTY;

        if (items.get(slot).isEmpty())
            return ItemStack.EMPTY;

        ItemStack item = getItem(slot);
        items.set(slot, ItemStack.EMPTY);
        return item;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        if (slot < 0 || slot >= items.size())
            return;

        items.set(slot, item);
    }

    @Override
    public void setChanged() {
        // Does nothing here
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return npc.isAlive() && npc.distanceToSqr(player) < 8 * 8;
    }

    public int getStorageSlots() {
        return storageSlots;
    }

    public int getArmorSlots() {
        return armorSlots;
    }

    public int getSelectedSlot() {
        return selected;
    }

    public void setSelectedSlot(int slot) {
        if (slot < 0 || slot >= storageSlots)
            slot = 0;

        selected = slot;
    }

    public int getArmorSlot(int relative) {
        return storageSlots + relative;
    }

    public int getOffhandSlot() {
        return storageSlots + armorSlots;
    }

    public int getFreeSlot() {
        for (int i = 0; i < storageSlots; i++) {
            if (items.get(i).isEmpty())
                return i;
        }

        if (items.get(getOffhandSlot()).isEmpty())
            return getOffhandSlot();

        return -1;
    }

    public int getItemSlotByType(EquipmentSlotType type) {
        switch (type) {

            case MAINHAND:
                return getSelectedSlot();

            case OFFHAND:
                return getOffhandSlot();

            case HEAD:
                return getArmorSlot(NPCInventory.HEAD_SLOT);

            case CHEST:
                return getArmorSlot(NPCInventory.BODY_SLOT);

            case LEGS:
                return getArmorSlot(NPCInventory.LEGS_SLOT);

            case FEET:
                return getArmorSlot(NPCInventory.FEET_SLOT);

            default:
                throw new IllegalStateException("This should never be called.");
        }
    }

}
