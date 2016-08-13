package ipixelmon;

import com.pixelmonmod.pixelmon.util.NBTTools;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public final class ItemSerializer {

    public static final String itemToString(final ItemStack stack) {
        final StringBuilder builder = new StringBuilder();

        builder.append("id=" + Item.getIdFromItem(stack.getItem()));
        builder.append("\n");
        builder.append("amount=" + stack.stackSize);
        builder.append("\n");
        builder.append("damage=" + stack.getItemDamage());
        builder.append("\n");

        if(stack.getEnchantmentTagList() != null) {
            NBTTagCompound tag;
            for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); ++i) {
                if (stack.getEnchantmentTagList().getCompoundTagAt(i) != null) {
                    tag = stack.getEnchantmentTagList().getCompoundTagAt(i);
                    if (tag != null && tag.hasKey("id") && tag.hasKey("lvl")) {
                        builder.append("ench=");
                        builder.append(tag.getShort("id"));
                        builder.append(",");
                        builder.append(tag.getShort("lvl"));
                        builder.append("\n");
                    }
                }
            }
        }

        return builder.toString();
    }

    public static final ItemStack itemFromString(final String str) {
        final String[] data = str.split("\n");

        int id = -1, amount = -1, damage = -1;
        short[][] enchantments = new short[25][2];

        String key, value;

        int countID = 0;
        for(String s : data) {
            if(!s.isEmpty()) {
                if(s.contains("=")) {
                   key = s.split("\\=")[0];
                    value = s.split("\\=")[1];

                    switch(key) {
                        case "id": {
                            id = Integer.parseInt(value);
                            break;
                        }
                        case "amount": {
                            amount = Integer.parseInt(value);
                            break;
                        }
                        case "damage": {
                            damage = Integer.parseInt(value);
                            break;
                        }
                        case "ench": {
                            String[] enchData = value.split(",");
                            enchantments[countID][0] = Short.parseShort(enchData[0]);
                            enchantments[countID][1] = Short.parseShort(enchData[1]);
                            countID++;
                            break;
                        }
                    }
                }
            }
        }

        if(id == -1 || amount == -1 || damage == -1) return null;

        final ItemStack toReturn = new ItemStack(Item.getItemById(id), amount, damage);

        for(short[] s : enchantments) {
            toReturn.addEnchantment(Enchantment.getEnchantmentById(s[0]), s[1]);
        }

        return toReturn;
    }

}
