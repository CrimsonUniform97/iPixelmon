package com.ipixelmon.quest;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.*;

/**
 * Created by colby on 12/31/2016.
 */
public class Quest {

    private UUID questGiver;
    private List<ItemStack> itemsToObtain, itemsToReward;
    private List<PixelmonData> pixelmonToObtain, pixelmonToReward;
    private long pokeDollarReward;

    public Quest(UUID questGiver, long pokeDollarReward) {
        this.questGiver = questGiver;
        this.pokeDollarReward = pokeDollarReward;
        this.itemsToObtain = new ArrayList<>();
        this.itemsToReward = new ArrayList<>();
        this.pixelmonToObtain = new ArrayList<>();
        this.pixelmonToReward = new ArrayList<>();
    }

    public List<ItemStack> getItemsToObtain() {
        return itemsToObtain;
    }

    public List<ItemStack> getItemsToReward() {
        return itemsToReward;
    }

    public List<PixelmonData> getPixelmonToObtain() {
        return pixelmonToObtain;
    }

    public List<PixelmonData> getPixelmonToReward() {
        return pixelmonToReward;
    }

    public long getPokeDollarReward() {
        return pokeDollarReward;
    }

    public UUID getQuestGiver() {
        return questGiver;
    }

    public boolean completed(ItemStack[] inventory) {
        List<ItemStack> items = Arrays.asList(inventory);
        items.removeAll(Collections.singleton(null));

        Map<FoundItem, Integer> foundItems = new HashMap<>();

        for(ItemStack stack : items) {
            for(ItemStack obtain : itemsToObtain) {
                if(ItemStack.areItemsEqual(stack, obtain) && ItemStack.areItemStackTagsEqual(stack, obtain)) {
                    FoundItem foundItem = new FoundItem(stack);
                    foundItems.put(foundItem, foundItems.get(foundItem) + stack.stackSize);
                }
            }
        }

        for(ItemStack stack : itemsToObtain) {
            int amountFound = foundItems.get(new FoundItem(stack));
            if(amountFound < stack.stackSize) {
                return false;
            }

            foundItems.put(new FoundItem(stack), amountFound - stack.stackSize);
        }



        return true;
    }

    private class FoundItem implements Comparable<FoundItem> {
        private ItemStack stack;

        public FoundItem(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int compareTo(FoundItem o) {
            if(ItemStack.areItemsEqual(stack, o.stack) && ItemStack.areItemStackTagsEqual(stack, o.stack))
                return 0;
            return -999;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof FoundItem)) return false;
            FoundItem foundItem = (FoundItem) obj;
            return ItemStack.areItemsEqual(stack, foundItem.stack) &&
                    ItemStack.areItemStackTagsEqual(stack, foundItem.stack);
        }
    }
}
