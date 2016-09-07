package ipixelmon.landcontrol.server;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FenceDetector implements Runnable
{

    private World world;
    private BlockPos clickedPos;
    private List<BlockPos> blockPosList;

    public FenceDetector(World world, BlockPos pos)
    {
        this.world = world;
        this.clickedPos = pos;
        this.blockPosList = new ArrayList<>();
    }

    @Override
    public void run()
    {
        int initialCount = blockPosList.size();
        int finalCount = 0;
        BlockPos pos = this.clickedPos;
        boolean firstRound = true;

        while (finalCount - initialCount < (firstRound ? 3 : 2))
        {
            initialCount = blockPosList.size();

            BlockPos blockPos;

            for (int x = -1; x < 1; x++)
            {
                for (int z = -1; z < 1; z++)
                {
                    blockPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    if (!blockPosList.contains(blockPos) && isFenceBlock(world, blockPos))
                    {
                        blockPosList.add(blockPos);
                    }
                }
            }

            pos = blockPosList.get(blockPosList.size() - 1);
            finalCount = blockPosList.size();
            firstRound = false;
        }
    }

    private boolean isFenceBlock(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.getUnlocalizedName().toLowerCase().contains("fence");
    }
}
