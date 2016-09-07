package ipixelmon.landcontrol.server;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerListener
{

    @SubscribeEvent
    public void playerInteraction(PlayerInteractEvent event)
    {
        if(!isFenceBlock(event.world, event.pos))
        {
            return;
        }


    }

    private boolean isFenceBlock(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.getUnlocalizedName().toLowerCase().contains("fence");
    }

}
