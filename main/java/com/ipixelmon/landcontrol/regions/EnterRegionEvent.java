package com.ipixelmon.landcontrol.regions;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by colby on 1/10/2017.
 */
public class EnterRegionEvent extends Event {

    public EntityPlayerMP player;
    public Region region;

    public EnterRegionEvent(EntityPlayerMP player, Region region) {
        this.player = player;
        this.region = region;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

}
