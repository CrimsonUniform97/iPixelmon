package com.ipixelmon.landcontrol.client;

import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by colby on 1/7/2017.
 */
public class PlayerListener {

    public static ToolCupboardTileEntity selectedTile;

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (selectedTile != null) {
            Chunk chunk = selectedTile.getWorld().getChunkFromBlockCoords(selectedTile.getPos());

            int x = chunk.xPosition * 16;
            int y = selectedTile.getPos().getY();
            int z = chunk.zPosition * 16;

            int color = 0xffee00;

            GuiUtil.drawSquareInWorld(x + 16, y, z, 16, 1, 180f, event.partialTicks, color);
            GuiUtil.drawSquareInWorld(x, y, z, 16, 1, 0f, event.partialTicks, color);

            GuiUtil.drawSquareInWorld(x + 16, y, z + 16, 16, 1, 90f, event.partialTicks, color);
            GuiUtil.drawSquareInWorld(x + 16, y, z, 16, 1, -90f, event.partialTicks, color);

            GuiUtil.drawSquareInWorld(x, y, z + 16, 16, 1, 0f, event.partialTicks, color);
            GuiUtil.drawSquareInWorld(x + 16, y, z + 16, 16, 1, 180f, event.partialTicks, color);

            GuiUtil.drawSquareInWorld(x, y, z, 16, 1, -90f, event.partialTicks, color);
            GuiUtil.drawSquareInWorld(x , y, z + 16, 16, 1, 90f, event.partialTicks, color);
        }
    }

}
