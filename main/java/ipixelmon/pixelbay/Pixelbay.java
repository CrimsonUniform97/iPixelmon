package ipixelmon.pixelbay;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.buy.PacketBuyItem;
import ipixelmon.pixelbay.gui.buy.PacketBuyPokemon;
import ipixelmon.pixelbay.gui.sell.PacketSellItem;
import ipixelmon.pixelbay.gui.sell.PacketSellPokemon;
import ipixelmon.pixelbay.gui.sell.PacketSellResponse;
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
        return ipixelmon.pixelbay.ClientProxy.class;
    }

    @Override
    public final Class<? extends CommonProxy> serverProxyClass() {
        return ipixelmon.pixelbay.ServerProxy.class;
    }

    @Override
    public final IGuiHandler getGuiHandler() {
        return new ipixelmon.pixelbay.GuiHandler();
    }

}
