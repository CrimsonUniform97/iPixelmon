package com.ipixelmon.team.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.team.EnumTeam;
import com.ipixelmon.team.PacketClientTeam;
import com.ipixelmon.team.TeamMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerListener
{

    @SubscribeEvent
    public void onChatRecieved(ServerChatEvent event)
    {
        String toDisplay = TeamMod.getPlayerTeam(event.getPlayer().getUniqueID()).colorChat() + event.getPlayer().getName() + " " + TextFormatting.GRAY + event.getMessage();
        event.setComponent(new TextComponentString(toDisplay));
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        // TODO: Not working some times
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        if(TeamMod.getPlayerTeam(player.getUniqueID()) == EnumTeam.None)
        {
            iPixelmon.network.sendTo(new PacketOpenTeamMenu(), player);
        } else {
            iPixelmon.network.sendTo(new PacketClientTeam(TeamMod.getPlayerTeam(player.getUniqueID())), player);
            player.setCustomNameTag(TeamMod.getPlayerTeam(player.getUniqueID()).colorChat().toString() + player.getName());
        }
    }

}
