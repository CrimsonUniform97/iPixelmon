package com.ipixelmon.pixelbay;

import com.ipixelmon.pixelbay.gui.sell.PacketSellItem;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.buy.PacketBuyItem;
import com.ipixelmon.pixelbay.gui.buy.PacketBuyPokemon;
import com.ipixelmon.pixelbay.gui.sell.PacketSellItem;
import com.ipixelmon.pixelbay.gui.sell.PacketSellPokemon;
import com.ipixelmon.pixelbay.gui.sell.PacketSellResponse;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

public final class Pixelbay implements IMod {

    @Override
    public final String getID() {
        return "pixelbay";
    }

    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {
        iPixelmon.registerPacket(PacketSellPokemon.Handler.class, PacketSellPokemon.class, Side.SERVER);
        iPixelmon.registerPacket(PacketSellItem.Handler.class, PacketSellItem.class, Side.SERVER);
        iPixelmon.registerPacket(PacketBuyItem.Handler.class, PacketBuyItem.class, Side.SERVER);
        iPixelmon.registerPacket(PacketBuyPokemon.Handler.class, PacketBuyPokemon.class, Side.SERVER);
        iPixelmon.registerPacket(PacketSellResponse.Handler.class, PacketSellResponse.class, Side.CLIENT);
    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {

    }

    @Override
    public final Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public final Class<? extends CommonProxy> serverProxyClass() {
        return ServerProxy.class;
    }

    @Override
    public final IGuiHandler getGuiHandler() {
        return new GuiHandler();
    }

}
