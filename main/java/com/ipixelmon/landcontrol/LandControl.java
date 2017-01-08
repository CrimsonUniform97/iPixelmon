package com.ipixelmon.landcontrol;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.client.ClientProxy;
import com.ipixelmon.landcontrol.packet.PacketBindNetwork;
import com.ipixelmon.landcontrol.packet.PacketEditPlayer;
import com.ipixelmon.landcontrol.packet.PacketGuiResponse;
import com.ipixelmon.landcontrol.packet.PacketOpenGui;
import com.ipixelmon.landcontrol.server.ServerProxy;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardBlock;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardItem;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by colby on 12/31/2016.
 */
public class LandControl implements IMod {

    @Override
    public String getID() {
        return "landcontrol";
    }

    @Override
    public void preInit() {
        GameRegistry.registerTileEntity(ToolCupboardTileEntity.class, "tileEntityCupboard");
        GameRegistry.registerBlock(ToolCupboardBlock.instance);
        GameRegistry.registerItem(ToolCupboardItem.instance);

        iPixelmon.registerPacket(PacketOpenGui.Handler.class, PacketOpenGui.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketEditPlayer.Handler.class, PacketEditPlayer.class, Side.SERVER);
        iPixelmon.registerPacket(PacketGuiResponse.Handler.class, PacketGuiResponse.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketBindNetwork.Handler.class, PacketBindNetwork.class, Side.SERVER);
    }

    @Override
    public void init() {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }

}
