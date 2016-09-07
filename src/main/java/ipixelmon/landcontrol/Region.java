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
    private BlockPos min, max;

    public Region(World world, BlockPos pos) throws Exception
    {
        this.world = world;

        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + world.getWorldInfo().getWorldName() + "' " +
                "AND 'xMin' < '" + pos.getX() + "' AND 'xMax' > '" + pos.getX() + "' " +
                "AND 'zMin' < '" + pos.getZ() + "' AND 'zMax' > '" + pos.getZ() + "';");

        // TODO: Test
        if (result.next())
        {
            owner = UUID.fromString(result.getString("owner"));
            min = new BlockPos(result.getInt("xMin"), 0, result.getInt("zMin"));
            max = new BlockPos(result.getInt("xMax"), world.getHeight(), result.getInt("zMax"));
        } else
        {
            throw new Exception("There is no region there.");
        }
    }


}
