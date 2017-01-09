package com.ipixelmon.landcontrol.regions.packet;

import com.ipixelmon.landcontrol.client.gui.RegionGui;
import com.ipixelmon.landcontrol.regions.Region;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/8/2017.
 */
public class PacketOpenRegionGui implements IMessage {

    private Region region;

    public PacketOpenRegionGui() {
    }

    public PacketOpenRegionGui(Region region) {
        this.region = region;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        region = Region.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        region.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketOpenRegionGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenRegionGui message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void onMessage(PacketOpenRegionGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new RegionGui(message.region));
                }
            });
        }

    }

}
