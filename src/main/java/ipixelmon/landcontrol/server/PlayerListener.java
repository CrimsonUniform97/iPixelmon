package ipixelmon.landcontrol.server;

import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.LandControl;
import ipixelmon.mysql.InsertForm;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerListener
{

    @SubscribeEvent
    public void playerInteraction(PlayerInteractEvent event)
    {
        if (!isFenceBlock(event.world, event.pos))
        {
            return;
        }

        if (event.entityPlayer.getHeldItem().getItem() != Items.bone)
        {
            return;
        }
        new Thread(new FenceDetector(event.world, event.pos, event.entityPlayer)).start();
    }

    private boolean isFenceBlock(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.getUnlocalizedName().toLowerCase().contains("fence");
    }

}
