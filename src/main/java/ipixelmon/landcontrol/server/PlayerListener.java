package ipixelmon.landcontrol.server;

import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.LandControl;
import ipixelmon.landcontrol.Region;
import ipixelmon.mysql.InsertForm;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
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
        try
        {
            Region region = Region.getRegionAt(event.world, event.pos);

            if (region != null)
            {
                if (!region.isMember(event.entityPlayer))
                {
                    event.setCanceled(true);
                    event.entityPlayer.addChatComponentMessage(new ChatComponentText("You are not a member of that region."));
                    return;
                }
            }
        } catch (Exception e)
        {
        }

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

    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event)
    {
        try
        {
            Region region = Region.getRegionAt(event.world, event.pos);

            if (region != null)
            {
                if (!region.isMember(event.getPlayer()))
                {
                    event.setCanceled(true);
                    event.getPlayer().addChatComponentMessage(new ChatComponentText("You are not a member of that region."));
                }
            }
        } catch (Exception e)
        {
        }
    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.PlaceEvent event)
    {
        try
        {
            Region region = Region.getRegionAt(event.world, event.pos);

            if (region != null)
            {
                if (!region.isMember(event.player))
                {
                    event.setCanceled(true);
                    event.player.addChatComponentMessage(new ChatComponentText("You are not a member of that region."));
                }
            }
        } catch (Exception e)
        {
        }
    }


    private boolean isFenceBlock(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.getUnlocalizedName().toLowerCase().contains("fence");
    }

}
