package com.ipixelmon.team.client;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderListener {

    @SubscribeEvent
    public void onRenderPlayer(PlayerEvent.NameFormat event) {
        event.setDisplayname(event.getEntityPlayer().getCustomNameTag());
    }

}
