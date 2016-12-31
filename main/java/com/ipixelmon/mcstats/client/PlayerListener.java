package com.ipixelmon.mcstats.client;

import com.ipixelmon.mcstats.McStatsAPI;
import com.ipixelmon.util.GuiUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class PlayerListener {

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        if(McStatsAPI.Client.renderPos != null && McStatsAPI.Client.expAnimation != null) {
            McStatsAPI.Client.expAnimation.update(0, 1);

            GuiUtil.RenderFloatingText(new String[]{"+" + McStatsAPI.Client.renderEXP},
                    McStatsAPI.Client.renderPos.getX() + 0.52f,
                    McStatsAPI.Client.renderPos.getY() + 1 + (float) McStatsAPI.Client.expAnimation.posY - 0.02f,
                    McStatsAPI.Client.renderPos.getZ() + 0.52f, 0x000000, false, event.partialTicks, 0.10f);

            GuiUtil.RenderFloatingText(new String[]{"+" + McStatsAPI.Client.renderEXP},
                    McStatsAPI.Client.renderPos.getX() + 0.5f,
                    McStatsAPI.Client.renderPos.getY() + 1 + (float) McStatsAPI.Client.expAnimation.posY,
                    McStatsAPI.Client.renderPos.getZ() + 0.5f, 0xffea35, false, event.partialTicks, 0.10f);

            if(McStatsAPI.Client.expAnimation.done) {
                McStatsAPI.Client.expAnimation = null;
                McStatsAPI.Client.renderPos = null;
            }
        }
    }


}
