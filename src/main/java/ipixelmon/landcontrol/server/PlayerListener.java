package ipixelmon.landcontrol.server;

import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.PacketOpenRegionInfo;
import ipixelmon.landcontrol.Region;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerListener
{

    @SubscribeEvent
    public void playerInteraction(PlayerInteractEvent event)
    {
        // TODO: Add listener for client player. Send packet to player upon joining world that tells them what world they are in.
        // TODO: Then get region with String world name and position, and cancel it on client side so it's seemless. No block break,
        // TODO: then reappear. Already made Region.getRegionForClient
        try
        {
            Region region = new Region(event.world, event.pos);

            if (region != null)
            {
                if (!region.isMember(event.entityPlayer))
                {
                    event.setCanceled(true);
                    event.entityPlayer.addChatComponentMessage(new ChatComponentText("You are not a member of that region."));
                    return;
                }

                if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == Items.feather)
                {
                    iPixelmon.network.sendTo(new PacketOpenRegionInfo(region), (EntityPlayerMP) event.entityPlayer);
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
            Region region = new Region(event.world, event.pos);

            if (region != null)
            {
                System.out.println("BOOM");
                if (!region.isMember(event.getPlayer()))
                {
                    System.out.println("BOOM");
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
            Region region = new Region(event.world, event.pos);

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
