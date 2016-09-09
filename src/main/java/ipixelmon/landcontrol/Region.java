package ipixelmon.landcontrol;

import ipixelmon.iPixelmon;
import ipixelmon.mysql.UpdateForm;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Region
{

    private UUID owner;
    private List<UUID> members;
    private String world;
    private BlockPos min, max;
    private UUID id;

    private Region(String parWorld, BlockPos pos) throws Exception
    {
        world = parWorld;
        members = new ArrayList<>();

        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + world + "' " +
                "AND xMin <= '" + pos.getX() + "' AND xMax >= '" + pos.getX() + "' " +
                "AND zMin <= '" + pos.getZ() + "' AND zMax >= '" + pos.getZ() + "';");

        if (result.next())
        {
            owner = UUID.fromString(result.getString("owner"));
            min = new BlockPos(result.getInt("xMin"), 0, result.getInt("zMin"));
            max = new BlockPos(result.getInt("xMax"), 50, result.getInt("zMax"));
            members.add(owner);

            String membersStr = result.getString("members");

            if(membersStr != null && !membersStr.isEmpty())
            {
                if(!membersStr.contains(","))
                {
                    members.add(UUID.fromString(membersStr));
                } else
                {
                    for(String s : membersStr.split(","))
                    {
                        members.add(UUID.fromString(s));
                    }
                }
            }
        } else
        {
            throw new Exception("There is no region there.");
        }

        if (!LandControl.regions.contains(this))
        {
            LandControl.regions.add(this);
        }
    }

    public static Region getRegionAt(World world, BlockPos pos) throws Exception
    {
        for (Region r : LandControl.regions)
        {
            if (r.world.equals(world.getWorldInfo().getWorldName()))
            {
                if (r.isWithin(pos))
                {
                    return r;
                }
            }
        }

        return new Region(world.getWorldInfo().getWorldName(), pos);
    }

    public static Region getRegionAt(String world, BlockPos pos) throws Exception
    {
        for (Region r : LandControl.regions)
        {
            if (r.world.equals(world))
            {
                if (r.isWithin(pos))
                {
                    return r;
                }
            }
        }

        return new Region(world, pos);
    }

    public UUID getOwner()
    {
        return owner;
    }

    public List<UUID> getMembers()
    {
        return members;
    }

    public BlockPos getMin()
    {
        return min;
    }

    public BlockPos getMax()
    {
        return max;
    }

    public String getWorld()
    {
        return world;
    }

    public UUID getId()
    {
        return id;
    }

    public void setOwner(UUID player)
    {
        owner = player;

        UpdateForm updateForm = new UpdateForm("Regions");
        updateForm.set("owner", player.toString()).where("world", world);
        updateForm.where("xMin", min.getX()).where("xMax", max.getX());
        updateForm.where("zMin", min.getZ()).where("zMax", max.getZ());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public void addMember(UUID player)
    {
        members.add(player);

        UpdateForm updateForm = new UpdateForm("Regions");
        updateForm.set("members", membersToString()).where("world", world);
        updateForm.where("xMin", min.getX()).where("xMax", max.getX());
        updateForm.where("zMin", min.getZ()).where("zMax", max.getZ());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public void removeMember(UUID player)
    {
        members.remove(player);

        UpdateForm updateForm = new UpdateForm("Regions");
        updateForm.set("members", membersToString()).where("world", world);
        updateForm.where("xMin", min.getX()).where("xMax", max.getX());
        updateForm.where("zMin", min.getZ()).where("zMax", max.getZ());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public boolean isWithin(BlockPos pos)
    {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public boolean isMember(EntityPlayer player)
    {
        return members.contains(player.getUniqueID());
    }

    private String membersToString()
    {
        StringBuilder builder = new StringBuilder();

        if(members.isEmpty())
        {
            return "";
        }

        for(UUID member : members)
        {
            builder.append(member.toString());
            builder.append(",");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
