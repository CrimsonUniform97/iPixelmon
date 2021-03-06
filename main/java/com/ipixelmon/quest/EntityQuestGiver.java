package com.ipixelmon.quest;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.quest.packet.PacketQuestInfoToClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class EntityQuestGiver extends EntityLiving {

    private Map<UUID, Quest> activeQuests;

    public EntityQuestGiver(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.activeQuests = new HashMap<>();
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        if(worldObj.isRemote) return true;

        if(activeQuests.containsKey(player.getUniqueID())) {
            if(activeQuests.get(player.getUniqueID()).completed(player.inventory.mainInventory)) {
                // TODO
            } else {
                player.addChatComponentMessage(new TextComponentString("You have not completed your current quest."));
            }
            return false;
        }

        Quest quest = new Quest(this.getUniqueID(), 400);
        quest.getItemsToObtain().add(new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 12));
        quest.getItemsToReward().add(new ItemStack(Items.DIAMOND, 64));
        quest.getItemsToReward().add(new ItemStack(Items.GOLD_INGOT, 64));
        quest.getItemsToReward().add(new ItemStack(Items.DIAMOND_SWORD, 1));
        quest.getItemsToReward().add(new ItemStack(Items.DIAMOND_PICKAXE, 1));
        ItemStack enchanted = new ItemStack(Items.GOLDEN_SWORD,1);
//        enchanted.addEnchantment(Enchantment.fireAspect, 5);
        quest.getItemsToReward().add(enchanted);

        iPixelmon.network.sendTo(new PacketQuestInfoToClient(quest), (EntityPlayerMP) player);

        return super.processInteract(player, hand, stack);
    }


    /**
     * Disables damage to the entity by not having any code inside method
     */
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    }

}
