package com.ipixelmon.gyms.server;

import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by colby on 10/10/2016.
 */
public class PixelmonSendOutListener {

    @SubscribeEvent
    public void onDelivery(PixelmonSendOutEvent event) {
        System.out.println("CALLED");
    }

}
