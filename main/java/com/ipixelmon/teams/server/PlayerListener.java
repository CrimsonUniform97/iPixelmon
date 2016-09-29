package com.ipixelmon.teams.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.teams.EnumTeam;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.teams.EnumTeam;
import com.ipixelmon.teams.Teams;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class PlayerListener
{

    @SubscribeEvent
    public void onChatRecieved(ServerChatEvent event)
    {
        String toDisplay = Teams.getPlayerTeam(event.player.getUniqueID()).color() + event.player.getName() + " " + EnumChatFormatting.GRAY + event.message;
        event.setComponent(new ChatComponentText(toDisplay));
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(Teams.getPlayerTeam(event.player.getUniqueID()) == EnumTeam.None)
        {
            iPixelmon.network.sendTo(new PacketOpenTeamMenu(), (EntityPlayerMP) event.player);
        }
    }

}
