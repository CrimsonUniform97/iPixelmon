package com.ipixelmon.teams;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.teams.client.ClientProxy;
import com.ipixelmon.gyms.EntityGymLeader;
import com.ipixelmon.teams.client.PacketChooseTeam;
import com.ipixelmon.teams.server.PacketOpenTeamMenu;
import com.ipixelmon.teams.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.sql.ResultSet;
import java.util.UUID;

public class Teams implements IMod
{

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
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ServerProxy.class;
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
