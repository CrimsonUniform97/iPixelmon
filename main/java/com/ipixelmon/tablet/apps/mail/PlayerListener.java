package com.ipixelmon.tablet.apps.mail;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by colby on 12/14/2016.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // TODO: Look up players send them mail. If they end up being the last on the mail list remove it from the mysql database
    }

}
