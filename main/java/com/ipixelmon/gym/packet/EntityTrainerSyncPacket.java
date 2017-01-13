package com.ipixelmon.gym.packet;

import com.ipixelmon.gym.EntityTrainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTrainerSyncPacket implements IMessage {

    private int entityID;
    private NBTTagCompound tagCompound;

    public EntityTrainerSyncPacket() {
    }

    public EntityTrainerSyncPacket(int entityID, NBTTagCompound tagCompound) {
        this.entityID = entityID;
        this.tagCompound = tagCompound;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityID = buf.readInt();
        tagCompound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityID);
        ByteBufUtils.writeTag(buf, tagCompound);
    }

    public static class Handler implements IMessageHandler<EntityTrainerSyncPacket, IMessage> {

        @Override
        public IMessage onMessage(EntityTrainerSyncPacket message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void onMessage(EntityTrainerSyncPacket message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityTrainer trainer = (EntityTrainer) Minecraft.getMinecraft().theWorld.getEntityByID(message.entityID);
                    if(trainer != null) trainer.readFromNBT(message.tagCompound);
                }
            });
        }

    }

}
