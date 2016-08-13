package ipixelmon;

import com.google.common.base.Splitter;
import com.pixelmonmod.pixelmon.util.NBTTools;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
        int id = -1, amount = -1, damage = -1;
        short[][] enchantments = new short[25][2];

        Iterator<String> it = Splitter.on(",").split(str).iterator();

        id = Integer.parseInt(it.next());
        amount = Integer.parseInt(it.next());
        damage = Integer.parseInt(it.next());

        int count = 0;
        while(it.hasNext()) {
            enchantments[count][0] = Short.parseShort(it.next());
            enchantments[count][1] = Short.parseShort(it.next());
            count++;
        }

        if (id == -1 || amount == -1 || damage == -1) return null;

        final ItemStack toReturn = new ItemStack(Item.getItemById(id), amount, damage);

        for (short[] s : enchantments) toReturn.addEnchantment(Enchantment.getEnchantmentById(s[0]), s[1]);

        return toReturn;
    }

}
