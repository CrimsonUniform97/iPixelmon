package com.ipixelmon.itemdisplay;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ItemDisplayMod implements IMod {

    @Override
    public String getID() {
        return "itemDisplayMod";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.register(ItemDisplayBlock.instance);
        GameRegistry.register(new ItemBlock(ItemDisplayBlock.instance), ItemDisplayBlock.instance.getRegistryName());
        GameRegistry.registerTileEntity(ItemDisplayBlockTileEntity.class, "itemDisplayBlockTileEntity");

        iPixelmon.registerPacket(PacketOpenGui.Handler.class, PacketOpenGui.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketUpdateTileEntity.Handler.class, PacketUpdateTileEntity.class, Side.SERVER);
    }

    @Override
    public void init(FMLInitializationEvent event) {

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
        return null;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }
}
