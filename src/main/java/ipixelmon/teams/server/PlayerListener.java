package ipixelmon.teams.server;

import ipixelmon.iPixelmon;
import ipixelmon.teams.EnumTeam;
import ipixelmon.teams.Teams;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerListener
{

    @SubscribeEvent
    public void onChatRecieved(ServerChatEvent event)
    {
        String toDisplay = Teams.getPlayerTeam(event.player.getUniqueID()) + event.player.getName() + " " + EnumChatFormatting.GRAY + event.message;
        event.setComponent(new ChatComponentText(toDisplay));
    }

    // TODO: Kick player if they move and haven't picked a team.
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event)
    {

    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(Teams.getPlayerTeam(event.player.getUniqueID()) == EnumTeam.None)
        {
            System.out.println("CALLED");
            iPixelmon.network.sendTo(new PacketOpenTeamMenu(), (EntityPlayerMP) event.player);
        }
    }

}
