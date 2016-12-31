package com.ipixelmon.quest.packet;

import com.ipixelmon.quest.Quest;
import com.ipixelmon.quest.client.GuiQuest;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Created by colby on 12/31/2016.
 */
public class PacketQuestInfoToClient implements IMessage {

    public PacketQuestInfoToClient() {
    }

    private Quest quest;

    public PacketQuestInfoToClient(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        UUID questGiverUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        long pokeDollarReward = buf.readLong();
        quest = new Quest(questGiverUUID, pokeDollarReward);

        int toObtain = buf.readInt();
        for (int i = 0; i < toObtain; i++) {
            quest.getItemsToObtain().add(ByteBufUtils.readItemStack(buf));
        }

        int toReward = buf.readInt();
        for (int i = 0; i < toReward; i++) {
            quest.getItemsToReward().add(ByteBufUtils.readItemStack(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, quest.getQuestGiver().toString());
        buf.writeLong(quest.getPokeDollarReward());

        buf.writeInt(quest.getItemsToObtain().size());
        for (ItemStack stack : quest.getItemsToObtain()) {
            ByteBufUtils.writeItemStack(buf, stack);
        }

        buf.writeInt(quest.getItemsToReward().size());
        for (ItemStack stack : quest.getItemsToReward()) {
            ByteBufUtils.writeItemStack(buf, stack);
        }
    }

    public static class Handler implements IMessageHandler<PacketQuestInfoToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketQuestInfoToClient message, MessageContext ctx) {
            openGui(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void openGui(PacketQuestInfoToClient message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiQuest(message.quest));
                }
            });

        }
    }
}
