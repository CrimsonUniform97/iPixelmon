package ipixelmon.landcontrol.server;

import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.PacketOpenRegionInfo;
import ipixelmon.landcontrol.Region;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
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
        if (event.pos.getX() == 0 && event.pos.getY() == 0 && event.pos.getZ() == 0)
        {
            return;
        }

        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        Region region = null;
        Exception exception = null;
        try
        {
            region = new Region(event.world, event.pos);
        } catch (Exception e)
        {
            exception = e;
        }

        if (event.entityPlayer.getHeldItem() != null)
        {
            if (event.entityPlayer.getHeldItem().getItem() == Items.feather)
            {
                if (region == null)
                {
                    event.entityPlayer.addChatComponentMessage(new ChatComponentText(exception.getMessage()));
                    return;
                }
                iPixelmon.network.sendTo(new PacketOpenRegionInfo(region), (EntityPlayerMP) event.entityPlayer);
            } else if (event.entityPlayer.getHeldItem().getItem() == Items.bone)
            {
                if (region != null)
                {
                    event.entityPlayer.addChatComponentMessage(new ChatComponentText("There is a region there."));
                    return;
                }

                MinecraftServer.getServer().addScheduledTask(new FenceDetector(event.world, event.pos, event.entityPlayer));
            }
        } else
        {
            if (region == null)
            {
                return;
            }

            if (!region.isMember(event.entityPlayer))
            {
                event.entityPlayer.addChatComponentMessage(new ChatComponentText("You do not have permission for this area."));
                event.setCanceled(true);
            }

        }

    }

    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event)
    {

        Region region = null;
        try
        {
            region = new Region(event.world, event.pos);
        } catch (Exception e)
        {
        }

        if (region != null)
        {
            if (!region.isMember(event.getPlayer()))
            {
                event.setCanceled(true);
                event.getPlayer().addChatComponentMessage(new ChatComponentText("You are not a member of that region."));
            }
        }

    }

    @SubscribeEvent
    public void placeBlock(BlockEvent.PlaceEvent event)
    {

        Region region = null;
        try
        {
            region = new Region(event.world, event.pos);
        } catch (Exception e)
        {
        }

        if (region != null)
        {
            if (!region.isMember(event.player))
            {
                event.setCanceled(true);
                event.player.addChatComponentMessage(new ChatComponentText("You are not a member of that region."));
            }
        }

    }


    private boolean isFenceBlock(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.getUnlocalizedName().toLowerCase().contains("fence");
    }

}
