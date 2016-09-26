package ipixelmon.teams;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.SelectionForm;
import ipixelmon.teams.client.PacketChooseTeam;
import ipixelmon.teams.server.PacketOpenTeamMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.sql.ResultSet;
import java.util.UUID;

public class Teams implements IMod
{
    // TODO: Use different team names, from GoogleTranslate

    @Override
    public String getID()
    {
        return "teams";
    }

    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {
        iPixelmon.registerPacket(PacketOpenTeamMenu.Handler.class, PacketOpenTeamMenu.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketChooseTeam.Handler.class, PacketChooseTeam.class, Side.SERVER);
    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass()
    {
        return ipixelmon.teams.client.ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ipixelmon.teams.server.ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler()
    {
        return null;
    }

    public static EnumTeam getPlayerTeam(UUID playerUUID)
    {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Teams.class, new SelectionForm("Players").add("uuid", playerUUID.toString()));

        try
        {
            if (result.next())
            {
                return EnumTeam.valueOf(result.getString("team"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return EnumTeam.None;
    }
}
