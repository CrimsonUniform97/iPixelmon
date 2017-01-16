package com.ipixelmon.team;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.team.client.ClientProxy;
import com.ipixelmon.team.client.PacketChooseTeam;
import com.ipixelmon.team.server.PacketOpenTeamMenu;
import com.ipixelmon.team.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.util.UUID;

public class TeamMod implements IMod
{

    protected static EnumTeam clientSideTeam = EnumTeam.None;

    @Override
    public String getID()
    {
        return "team";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        iPixelmon.registerPacket(PacketOpenTeamMenu.Handler.class, PacketOpenTeamMenu.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketChooseTeam.Handler.class, PacketChooseTeam.class, Side.SERVER);
        iPixelmon.registerPacket(PacketClientTeam.Handler.class, PacketClientTeam.class, Side.CLIENT);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

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

    @SideOnly(Side.SERVER)
    public static EnumTeam getPlayerTeam(UUID playerUUID)
    {
        ResultSet result = iPixelmon.mysql.selectAllFrom(TeamMod.class, new SelectionForm("Players").where("uuid", playerUUID.toString()));

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

    @SideOnly(Side.CLIENT)
    public static EnumTeam getClientSideTeam() {
        return clientSideTeam;
    }
}
