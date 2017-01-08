package com.ipixelmon.landcontrol.packet;

import com.ipixelmon.TimedMessage;
import com.ipixelmon.landcontrol.client.gui.ToolCupboardGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/7/2017.
 */
public class PacketGuiResponse implements IMessage {

    private String message;

    public PacketGuiResponse() {
    }

    public PacketGuiResponse(String message) {

        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
    }

    public static class Handler implements IMessageHandler<PacketGuiResponse, IMessage> {

        @Override
        public IMessage onMessage(PacketGuiResponse message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void onMessage(PacketGuiResponse message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if (Minecraft.getMinecraft().currentScreen instanceof ToolCupboardGui) {
                        ToolCupboardGui cupboardGui = (ToolCupboardGui) Minecraft.getMinecraft().currentScreen;

                        if (message.message.equalsIgnoreCase("success")) {
                            cupboardGui.getPlayerField().setText("");
                        } else {
                            cupboardGui.timedMessage = new TimedMessage(message.message, 5);
                        }
                    }
                }
            });
        }
    }

}
