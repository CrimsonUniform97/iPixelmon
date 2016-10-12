package com.ipixelmon.landcontrol;

import com.ipixelmon.landcontrol.client.GuiRegionInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketOpenRegionInfo implements IMessage
{

    private UUID regionID;

    public PacketOpenRegionInfo()
    {
    }

    public PacketOpenRegionInfo(Region region)
    {
        this.regionID = region.id();
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        regionID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, regionID.toString());
    }

    public static class Handler implements IMessageHandler<PacketOpenRegionInfo, IMessage>
    {

        @Override
        public IMessage onMessage(final PacketOpenRegionInfo message, final MessageContext ctx)
        {
            openGui(message);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void openGui(final PacketOpenRegionInfo message)
    {
        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {

                Region region = null;
                try
                {
                    region =  LandControl.getRegion(message.regionID);
                } catch (Exception e)
                {
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(e.getMessage()));
                    return;
                }

                Minecraft.getMinecraft().displayGuiScreen(new GuiRegionInfo(region));

            }
        });
    }
}
