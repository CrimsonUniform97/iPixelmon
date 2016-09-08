package ipixelmon.landcontrol.server;

import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.LandControl;
import ipixelmon.mysql.InsertForm;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FenceDetector implements Runnable
{

    private World world;
    private BlockPos clickedPos, minPos, maxPos;
    private List<BlockPos> blockPosList;
    private EntityPlayer player;

    public FenceDetector(World world, BlockPos pos, EntityPlayer player)
    {
        this.world = world;
        this.clickedPos = pos;
        this.player = player;
        this.blockPosList = new ArrayList<>();
        this.blockPosList.add(pos);
    }

    @Override
    public void run()
    {

        BlockPos currentBlock = this.clickedPos;
        List<BlockPos> surroundingFences = new ArrayList<>();
        surroundingFences.add(currentBlock);

        while (!surroundingFences.isEmpty() && surroundingFences.size() < 3)
        {
            surroundingFences.clear();

            BlockPos blockPos;
            for (int i = -1; i <= 1; i++)
            {

                blockPos = new BlockPos(currentBlock.getX() + i, this.clickedPos.getY(), currentBlock.getZ());
                if (!world.isAirBlock(blockPos))
                {
                    if (isFenceBlock(world, blockPos) && !blockPosList.contains(blockPos) && !surroundingFences.contains(blockPos))
                    {
                        surroundingFences.add(blockPos);
                    }
                }
            }

            for (int i = -1; i <= 1; i++)
            {

                blockPos = new BlockPos(currentBlock.getX(), this.clickedPos.getY(), currentBlock.getZ() + i);
                if (!world.isAirBlock(blockPos))
                {
                    if (isFenceBlock(world, blockPos) && !blockPosList.contains(blockPos) && !surroundingFences.contains(blockPos))
                    {
                        surroundingFences.add(blockPos);
                    }
                }
            }

            if (surroundingFences.isEmpty())
            {
                break;
            }

            if(surroundingFences.size() > 1 && blockPosList.size() > 1)
            {
                player.addChatComponentMessage(new ChatComponentText("Found outlier."));
                return;
            }

            currentBlock = surroundingFences.get(0);
            blockPosList.add(currentBlock);
        }


        // TODO: Need to make sure it's complete square some how.

        List<Integer> xPositions = new ArrayList<>();
        List<Integer> zPositions = new ArrayList<>();
        for (BlockPos pos : blockPosList)
        {
            xPositions.add(pos.getX());
            zPositions.add(pos.getZ());
        }

        Collections.sort(xPositions);
        Collections.sort(zPositions);

        minPos = new BlockPos(xPositions.get(0), 0, zPositions.get(0));
        maxPos = new BlockPos(xPositions.get(xPositions.size() - 1), world.getHeight(), zPositions.get(zPositions.size() - 1));


        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + world.getWorldInfo().getWorldName() + "' " +
                "AND xMin <= '" + maxPos.getX() + "' AND xMax >= '" + minPos.getX() + "' " +
                "AND zMin <= '" + maxPos.getZ() + "' AND zMax >= '" + minPos.getZ() + "';");

        try
        {
            if (result.next())
            {
                player.addChatComponentMessage(new ChatComponentText("There is a region already there."));
                return;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        InsertForm insertForm = new InsertForm("Regions");
        insertForm.add("owner", player.getUniqueID().toString());
        insertForm.add("members", "");
        insertForm.add("world", world.getWorldInfo().getWorldName());
        insertForm.add("xMin", minPos.getX());
        insertForm.add("xMax", maxPos.getX());
        insertForm.add("zMin", minPos.getZ());
        insertForm.add("zMax", maxPos.getZ());

        iPixelmon.mysql.insert(LandControl.class, insertForm);

        player.addChatComponentMessage(new ChatComponentText("Region created."));
    }

    private boolean isFenceBlock(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        return block.getUnlocalizedName().toLowerCase().contains("fence");
    }

}
