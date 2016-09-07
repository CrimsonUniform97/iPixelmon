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
        int initialCount = 0;
        int finalCount = blockPosList.size();
        List<BlockPos> toAdd = new ArrayList<>();
        boolean firstRun = true;
        while (finalCount - initialCount > 0)
        {
            initialCount = blockPosList.size();
            for (BlockPos blockPos : blockPosList)
            {
                BlockPos toCheck;
                for (int x = -1; x <= 1; x++)
                {
                    for (int z = -1; z <= 1; z++)
                    {
                        toCheck = new BlockPos(blockPos.getX() + x, clickedPos.getY(), blockPos.getZ() + z);

                        if (!blockPosList.contains(toCheck) && isFenceBlock(world, toCheck))
                        {
                            toAdd.add(toCheck);
                        }
                    }
                }
            }

            if(firstRun)
            {
                blockPosList.add(toAdd.get(0));
            } else
            {
                blockPosList.addAll(toAdd);
            }
            toAdd.clear();
            finalCount = blockPosList.size();
            firstRun = false;
        }

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

        // TODO: Fix the MySQL result, it's not finding the region if it overlaps
//        Xmin1 <= Xmax2 && Xmin2 <= Xmax1
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + world.getWorldInfo().getWorldName() + "' " +
                "AND 'xMin' < '" + maxPos.getX() + "' AND 'xMax' > '" + minPos.getX() + "' " +
                "AND 'zMin' < '" + maxPos.getZ() + "' AND 'zMax' > '" + minPos.getZ() + "';");

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
