package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppBase;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketPurchaseRequest;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketRequestPage;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketResultsToClient;
import com.ipixelmon.tablet.app.pixelbay.packet.sell.PacketSellItem;
import com.ipixelmon.tablet.app.pixelbay.packet.sell.PacketSellPixelmon;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayApp implements AppBase {

    @Override
    public void preInit() {
        iPixelmon.registerPacket(PacketSellItem.Handler.class, PacketSellItem.class, Side.SERVER);
        iPixelmon.registerPacket(PacketSellPixelmon.Handler.class, PacketSellPixelmon.class, Side.SERVER);
        iPixelmon.registerPacket(PacketResultsToClient.Handler.class, PacketResultsToClient.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketRequestPage.Handler.class, PacketRequestPage.class, Side.SERVER);
        iPixelmon.registerPacket(PacketPurchaseRequest.Handler.class, PacketPurchaseRequest.class, Side.SERVER);
    }

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "Pixelbay";
    }

    @Override
    public Class<? extends AppProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends AppProxy> serverProxyClass() {
        return ServerProxy.class;
    }
}
