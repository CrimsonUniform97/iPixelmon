package com.ipixelmon.landcontrol.regions.packet;

import com.ipixelmon.landcontrol.client.gui.RegionGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colbymchenry on 1/9/17.
 */
public class PacketModifyRegionResponse implements IMessage {

    private String response;

    public PacketModifyRegionResponse() {
    }

    public PacketModifyRegionResponse(String response) {
        this.response = response;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        response = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, response);
    }

    public static class Handler implements IMessageHandler<PacketModifyRegionResponse, IMessage> {

        @Override
        public IMessage onMessage(PacketModifyRegionResponse message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void onMessage(PacketModifyRegionResponse message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if(Minecraft.getMinecraft().currentScreen instanceof RegionGui) {
                        RegionGui gui = (RegionGui) Minecraft.getMinecraft().currentScreen;
                        gui.infoMessage.setMessage(message.response, 5);
                    }
                }
            });
        }

    }
}
