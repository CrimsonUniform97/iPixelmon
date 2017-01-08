package com.ipixelmon.landcontrol;

import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * Created by colby on 1/7/2017.
 */
public class LandControlAPI {

    @SideOnly(Side.CLIENT)
    public static class Client {

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static ToolCupboardTileEntity getTileEntity(World world, BlockPos pos) {
            Map<BlockPos, TileEntity> tileEntityMap = world.getChunkFromBlockCoords(pos).getTileEntityMap();
            for(BlockPos p : tileEntityMap.keySet()) {
                TileEntity tileEntity = tileEntityMap.get(p);
                if(tileEntity instanceof ToolCupboardTileEntity)
                    return ((ToolCupboardTileEntity) tileEntity).getBaseTile();
            }

            return null;
        }

    }

}
