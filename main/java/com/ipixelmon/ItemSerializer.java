package com.ipixelmon;

import com.google.common.base.Splitter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Iterator;

public final class ItemSerializer {

    public static final String itemToString(final ItemStack stack) {
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

    public static final ItemStack itemFromString(final String str) {
        Iterator<String> it = Splitter.on(",").split(str).iterator();

        final ItemStack toReturn = new ItemStack(Item.getItemById(Integer.parseInt(it.next())), Integer.parseInt(it.next()), Integer.parseInt(it.next()));

        while(it.hasNext()) {
            toReturn.addEnchantment(Enchantment.getEnchantmentById(Short.parseShort(it.next())), Short.parseShort(it.next()));
        }

        return toReturn;
    }

}
