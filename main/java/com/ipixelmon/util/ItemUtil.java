package com.ipixelmon.util;

import com.google.common.base.Splitter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemUtil {

    public final Iterator<ItemStackInfo> getPlayerInvIterator(EntityPlayerMP player) {
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

    public class ItemStackInfo {
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

    public final String itemToString(final ItemStack stack) {
        final StringBuilder builder = new StringBuilder();

        builder.append(Item.getIdFromItem(stack.getItem()));
        builder.append(",");
        builder.append(stack.stackSize);
        builder.append(",");
        builder.append(stack.getItemDamage());
        builder.append(",");

        if (stack.getEnchantmentTagList() != null) {
            NBTTagCompound tag;
            for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); ++i) {
                if (stack.getEnchantmentTagList().getCompoundTagAt(i) != null) {
                    tag = stack.getEnchantmentTagList().getCompoundTagAt(i);
                    if (tag != null && tag.hasKey("id") && tag.hasKey("lvl")) {
                        builder.append(tag.getShort("id"));
                        builder.append(",");
                        builder.append(tag.getShort("lvl"));
                        builder.append(",");
                    }
                }
            }
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public final ItemStack itemFromString(final String str) {
        Iterator<String> it = Splitter.on(",").split(str).iterator();

        final ItemStack toReturn = new ItemStack(Item.getItemById(Integer.parseInt(it.next())), Integer.parseInt(it.next()), Integer.parseInt(it.next()));

        while(it.hasNext()) {
            toReturn.addEnchantment(Enchantment.getEnchantmentById(Short.parseShort(it.next())), Short.parseShort(it.next()));
        }

        return toReturn;
    }

}
