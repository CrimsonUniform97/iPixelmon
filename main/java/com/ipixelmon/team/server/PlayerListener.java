package com.ipixelmon.team.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.team.EnumTeam;
import com.ipixelmon.team.PacketClientTeam;
import com.ipixelmon.team.TeamMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerListener
{

    @SubscribeEvent
    public void onChatRecieved(ServerChatEvent event)
    {
        String toDisplay = TeamMod.getPlayerTeam(event.player.getUniqueID()).colorChat() + event.player.getName() + " " + EnumChatFormatting.GRAY + event.message;
        event.setComponent(new ChatComponentText(toDisplay));
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(TeamMod.getPlayerTeam(event.player.getUniqueID()) == EnumTeam.None)
        {
            iPixelmon.network.sendTo(new PacketOpenTeamMenu(), (EntityPlayerMP) event.player);
        } else {
            iPixelmon.network.sendTo(new PacketClientTeam(TeamMod.getPlayerTeam(event.player.getUniqueID())), (EntityPlayerMP) event.player);
        }
    }

}
