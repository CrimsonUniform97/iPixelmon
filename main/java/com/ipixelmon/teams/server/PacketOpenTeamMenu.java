package com.ipixelmon.teams.server;

import com.ipixelmon.teams.client.GuiTeamMenu;
import io.netty.buffer.ByteBuf;
import com.ipixelmon.teams.client.GuiTeamMenu;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenTeamMenu implements IMessage
{

    public PacketOpenTeamMenu(){}

    @Override
    public void fromBytes(ByteBuf buf)
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {

    }

    public static class Handler implements IMessageHandler<PacketOpenTeamMenu, IMessage>
    {

        @Override
        public IMessage onMessage(PacketOpenTeamMenu message, MessageContext ctx)
        {
            openMenu(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private static void openMenu(PacketOpenTeamMenu message)
        {
            Minecraft.getMinecraft().addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiTeamMenu());
                }
            });
        }

    }

}
