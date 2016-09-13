package ipixelmon.teams.server;

import ipixelmon.teams.Teams;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerListener
{

    @SubscribeEvent
    public void onChatRecieved(ServerChatEvent event)
    {
        String toDisplay = Teams.getPlayerTeam(event.player.getUniqueID()) + event.player.getName() + " " + EnumChatFormatting.GRAY + event.message;
        event.setComponent(new ChatComponentText(toDisplay));
    }

}
