package ipixelmon.landcontrol;

import ipixelmon.iPixelmon;
import ipixelmon.mysql.UpdateForm;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Region
{

    private UUID uuid, owner;
    private List<UUID> members;
    private World world;
    private BlockPos min, max;

    @SideOnly(Side.SERVER)
    public Region(World parWorld, BlockPos pos) throws Exception
    {
        world = parWorld;
        members = new ArrayList<>();

        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + world + "' " +
                "AND xMin <= '" + pos.getX() + "' AND xMax >= '" + pos.getX() + "' " +
                "AND zMin <= '" + pos.getZ() + "' AND zMax >= '" + pos.getZ() + "';");

        if (result.next())
        {
            uuid = UUID.fromString(result.getString("uuid"));
            owner = UUID.fromString(result.getString("owner"));
            min = new BlockPos(result.getInt("xMin"), 0, result.getInt("zMin"));
            max = new BlockPos(result.getInt("xMax"), 50, result.getInt("zMax"));

            String membersStr = result.getString("members");

            if (membersStr != null && !membersStr.isEmpty())
            {
                if (!membersStr.contains(","))
                {
                    members.add(UUID.fromString(membersStr));
                } else
                {
                    for (String s : membersStr.split(","))
                    {
                        members.add(UUID.fromString(s));
                    }
                }
            }
        } else
        {
            throw new Exception("There is no region there.");
        }
    }

    public Region(UUID uuid) throws Exception
    {
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE uuid='" + uuid.toString() + "';");

        if (result.next())
        {
            this.uuid = uuid;
            owner = UUID.fromString(result.getString("owner"));
            min = new BlockPos(result.getInt("xMin"), 0, result.getInt("zMin"));
            max = new BlockPos(result.getInt("xMax"), 50, result.getInt("zMax"));

            String membersStr = result.getString("members");

            if (membersStr != null && !membersStr.isEmpty())
            {
                if (!membersStr.contains(","))
                {
                    members.add(UUID.fromString(membersStr));
                } else
                {
                    for (String s : membersStr.split(","))
                    {
                        members.add(UUID.fromString(s));
                    }
                }
            }
        } else
        {
            throw new Exception("There is no region there.");
        }
    }

    @SideOnly(Side.CLIENT)
    public static Region getRegionForClient(BlockPos pos) throws Exception
    {
        if(!Minecraft.getMinecraft().thePlayer.getEntityData().hasKey("regionWorld"))
        {
            return null;
        }

        String worldName = Minecraft.getMinecraft().thePlayer.getEntityData().getString("regionWorld");

        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE world='" + worldName + "' " +
                "AND xMin <= '" + pos.getX() + "' AND xMax >= '" + pos.getX() + "' " +
                "AND zMin <= '" + pos.getZ() + "' AND zMax >= '" + pos.getZ() + "';");

        if (result.next())
        {
            return new Region(UUID.fromString(result.getString("uuid")));
        } else
        {
            throw new Exception("There is no region there.");
        }
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

    @SideOnly(Side.SERVER)
    public World getWorld()
    {
        return world;
    }

    public String getWorldName()
    {
        if (world != null)
        {
            return world.getWorldInfo().getWorldName();
        }

        ResultSet result = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE uuid='" + uuid.toString() + "';");

        try
        {
            if (result.next())
            {
                return result.getString("world");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public void setOwner(UUID player)
    {
        owner = player;

        UpdateForm updateForm = new UpdateForm("Regions");
        updateForm.set("owner", player.toString()).where("uuid", uuid.toString());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public void addMember(UUID player)
    {
        if (!members.contains(player))
        {
            members.add(player);

            UpdateForm updateForm = new UpdateForm("Regions");
            updateForm.set("members", membersToString()).where("uuid", uuid.toString());
            iPixelmon.mysql.update(LandControl.class, updateForm);
        }
    }

    public void removeMember(UUID player)
    {
        if (members.contains(player) && !owner.equals(player))
        {
            members.remove(player);

            UpdateForm updateForm = new UpdateForm("Regions");
            updateForm.set("members", membersToString()).where("uuid", uuid.toString());
            iPixelmon.mysql.update(LandControl.class, updateForm);
        }
    }

    public boolean isWithin(BlockPos pos)
    {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public boolean isMember(EntityPlayer player)
    {
        return owner.equals(player.getUniqueID()) || members.contains(player.getUniqueID());
    }

    private String membersToString()
    {
        StringBuilder builder = new StringBuilder();

        if (members.isEmpty())
        {
            return "";
        }

        for (UUID member : members)
        {
            builder.append(member.toString());
            builder.append(",");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
