package ipixelmon.landcontrol;

import ipixelmon.iPixelmon;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public class Region
{

    private UUID owner;
    private List<UUID> members;
    private World world;
    private BlockPos pos;

    public Region(World world, BlockPos pos)
    {
        this.world = world;
        this.pos = pos;

        try
        {
            ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + world.getWorldInfo().getWorldName() + "' " +
                    "AND 'xMin' < '" + pos.getX() + "' AND 'xMax' > '" + pos.getX() + "' " +
                    "AND 'zMin' < '" + pos.getZ() + "' AND 'zMax' > '" + pos.getZ() + "';");

            // TODO> Next....
            if (result.next())
            {
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
