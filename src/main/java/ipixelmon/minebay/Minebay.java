package ipixelmon.minebay;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import ipixelmon.minebay.gui.sell.PacketSellItem;
import ipixelmon.minebay.gui.sell.PacketSellPokemon;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

public final class Minebay implements IMod {

    @Override
    public final String getID() {
        return "minebay";
    }

    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {
        iPixelmon.registerPacket(PacketSellPokemon.Handler.class, PacketSellPokemon.class, Side.SERVER);
        iPixelmon.registerPacket(PacketSellItem.Handler.class, PacketSellItem.class, Side.SERVER);
    }

    @Override
    public final Class<? extends CommonProxy> clientProxyClass() {
        return ipixelmon.minebay.ClientProxy.class;
    }

    @Override
    public final Class<? extends CommonProxy> serverProxyClass() {
        return ipixelmon.minebay.ServerProxy.class;
    }

    @Override
    public final IGuiHandler getGuiHandler() {
        return new ipixelmon.minebay.GuiHandler();
    }

}
