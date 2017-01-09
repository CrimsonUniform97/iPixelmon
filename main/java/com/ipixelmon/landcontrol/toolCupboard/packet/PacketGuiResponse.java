package com.ipixelmon.landcontrol.toolCupboard.packet;

import com.google.common.collect.Maps;
import com.ipixelmon.TimedMessage;
import com.ipixelmon.landcontrol.client.gui.ToolCupboardGui;
import com.ipixelmon.util.ArrayUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.UUID;

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
                        } else if (message.message.contains("update")) {
                            String data = message.message.split("\\=")[1];
                            String[] array = ArrayUtil.fromString(data);

                            Map<UUID, String> players = Maps.newHashMap();
                            for(String s : array)
                                players.put(UUID.fromString(s.split(";")[0]), s.split(";")[1]);

                            cupboardGui.updatePlayerList(players);
                        } else {
                            cupboardGui.timedMessage = new TimedMessage(message.message, 5);
                        }
                    }
                }
            });
        }
    }

}
