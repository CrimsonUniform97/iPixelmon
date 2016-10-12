package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.gui.GuiClaimGym;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 10/10/2016.
 */
public class PacketOpenClaimGui implements IMessage {

    public PacketOpenClaimGui() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketOpenClaimGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenClaimGui message, MessageContext ctx) {
            openGui();
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void openGui() {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiClaimGym());
                }
            });
        }
    }
}
