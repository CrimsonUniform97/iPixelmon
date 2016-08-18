package ipixelmon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ItemUtil {

    public static final Iterator<ItemStackInfo> getPlayerInvIterator(EntityPlayerMP player) {
        ItemStack[] itemStacks = new ItemStack[player.inventory.mainInventory.length + player.inventory.armorInventory.length];

        List<ItemStackInfo> itemStackList = new ArrayList();
        for (int i = 0; i < itemStacks.length; i++) {
            if (i < player.inventory.mainInventory.length) {
                itemStackList.add(new ItemStackInfo(player.inventory.mainInventory[i], i));
            } else {
                itemStackList.add(new ItemStackInfo(player.inventory.armorInventory[i - player.inventory.mainInventory.length], i));
            }
        }

        ItemStackInfo[] items = itemStackList.toArray(new ItemStackInfo[itemStackList.size()]);

        Iterator<ItemStackInfo> iterator = new Iterator<ItemStackInfo>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < items.length && items[index] != null;
            }

            @Override
            public ItemStackInfo next() {
                return items[index++];
            }
        };

        return iterator;
    }

    public static class ItemStackInfo {
        private ItemStack itemStack;
        private int index;

        public ItemStackInfo(ItemStack itemStack, int index) {
            this.itemStack = itemStack;
            this.index = index;
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
    }

}
