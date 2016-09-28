package com.ipixelmon;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ItemUtil {

    public static final Iterator<ItemStackInfo> getPlayerInvIterator(EntityPlayerMP player) {
        ItemStack[] itemStacks = new ItemStack[player.inventory.mainInventory.length + player.inventory.armorInventory.length];

        final List<ItemStackInfo> itemStackList = new ArrayList();
        for (int i = 0; i < itemStacks.length; i++) {
            if (i < player.inventory.mainInventory.length) {
                itemStackList.add(new ItemStackInfo(player.inventory.mainInventory[i], i, InventoryType.MAIN_INVENTORY));
            } else {
                itemStackList.add(new ItemStackInfo(player.inventory.armorInventory[i - player.inventory.mainInventory.length], i, InventoryType.ARMOR));
            }
        }

        Iterator<ItemStackInfo> iterator = new Iterator<ItemStackInfo>() {

            ItemStackInfo[] items = itemStackList.toArray(new ItemStackInfo[itemStackList.size()]);
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < items.length && items[index] != null;
            }

            @Override
            public ItemStackInfo next() {
                return items[index++];
            }

            @Override
            public void remove() {
                items[index] = null;
            }
        };

        return iterator;
    }

    public static class ItemStackInfo {
        private ItemStack itemStack;
        private int index;
        private InventoryType inventoryType;

        public ItemStackInfo(ItemStack itemStack, int index, InventoryType inventoryType) {
            this.itemStack = itemStack;
            this.index = index;
            this.inventoryType = inventoryType;
        }

        public ItemStack getItemStack() {
            return this.itemStack;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean itemStackEquals(ItemStack itemStack) {
            return ItemStack.areItemStacksEqual(itemStack, this.itemStack);
        }

        public void removeFromPlayersInventory(EntityPlayerMP player) {
            player.inventory.removeStackFromSlot(index);
        }

        public InventoryType getInventoryType() {
            return inventoryType;
        }
    }

    public enum InventoryType {
        MAIN_INVENTORY, ARMOR;
    }

}
