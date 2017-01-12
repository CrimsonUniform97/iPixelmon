package com.ipixelmon.landcontrol;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.client.ClientProxy;
import com.ipixelmon.landcontrol.regions.packet.PacketModifyRegionResponse;
import com.ipixelmon.landcontrol.toolCupboard.packet.*;
import com.ipixelmon.landcontrol.regions.CommandRegion;
import com.ipixelmon.landcontrol.server.ServerProxy;
import com.ipixelmon.landcontrol.regions.packet.PacketModifyRegion;
import com.ipixelmon.landcontrol.regions.packet.PacketOpenRegionGui;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardBlock;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardItem;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerTileEntity(ToolCupboardTileEntity.class, "tileEntityCupboard");
        GameRegistry.registerBlock(ToolCupboardBlock.instance);
        GameRegistry.registerItem(ToolCupboardItem.instance);

        iPixelmon.registerPacket(PacketOpenGui.Handler.class, PacketOpenGui.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketEditPlayer.Handler.class, PacketEditPlayer.class, Side.SERVER);
        iPixelmon.registerPacket(PacketGuiResponse.Handler.class, PacketGuiResponse.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketBindNetwork.Handler.class, PacketBindNetwork.class, Side.SERVER);

        iPixelmon.registerPacket(PacketOpenRegionGui.Handler.class, PacketOpenRegionGui.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketModifyRegion.Handler.class, PacketModifyRegion.class, Side.SERVER);
        iPixelmon.registerPacket(PacketModifyRegionResponse.Handler.class, PacketModifyRegionResponse.class, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event) {
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandRegion());
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
