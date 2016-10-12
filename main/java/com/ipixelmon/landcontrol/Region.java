package com.ipixelmon.landcontrol;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Region {

    private UUID uuid, owner;
    private List<UUID> members;
    private World world;
    private BlockPos min, max;
    private String worldName;

    protected Region(UUID id) throws Exception {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class, new SelectionForm("Regions").add("uuid", id.toString()));

        if (result.next()) {
            members = new ArrayList<>();
            uuid = UUID.fromString(result.getString("uuid"));
            owner = UUID.fromString(result.getString("owner"));
            min = new BlockPos(result.getInt("xMin"), 0, result.getInt("zMin"));
            max = new BlockPos(result.getInt("xMax"), 50, result.getInt("zMax"));
            worldName = result.getString("world");

            String membersStr = result.getString("members");

            if (membersStr != null && !membersStr.isEmpty()) {
                if (!membersStr.contains(",")) {
                    members.add(UUID.fromString(membersStr));
                } else {
                    for (String s : membersStr.split(",")) {
                        members.add(UUID.fromString(s));
                    }
                }
            }
        } else {
            throw new Exception("There is no region there.");
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public BlockPos getMin() {
        return min;
    }

    public BlockPos getMax() {
        return max;
    }

    @SideOnly(Side.SERVER)
    public World getWorldServer() {
        if (world == null) {
            ResultSet resultSet = iPixelmon.mysql.query("SELECT * FROM landcontrolRegions WHERE uuid='" + uuid.toString() + "';");

            try {
                if (resultSet.next()) {
                    for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
                        if (worldServer.getWorldInfo().getWorldName().equalsIgnoreCase(resultSet.getString("world"))) {
                            world = worldServer;
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return world;
    }

    public String getWorldClient() {
        return worldName;
    }

    public UUID id() {
        return uuid;
    }

    public void setOwner(UUID player) {
        owner = player;

        UpdateForm updateForm = new UpdateForm("Regions");
        updateForm.set("owner", player.toString()).where("uuid", uuid.toString());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public void addMember(UUID player) {
        if (!members.contains(player)) {
            members.add(player);

            UpdateForm updateForm = new UpdateForm("Regions");
            updateForm.set("members", membersToString()).where("uuid", uuid.toString());
            iPixelmon.mysql.update(LandControl.class, updateForm);
        }
    }

    public void removeMember(UUID player) {
        if (members.contains(player) && !owner.equals(player)) {
            members.remove(player);

            UpdateForm updateForm = new UpdateForm("Regions");
            updateForm.set("members", membersToString()).where("uuid", uuid.toString());
            iPixelmon.mysql.update(LandControl.class, updateForm);
        }
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public boolean isMember(EntityPlayer player) {
        return owner.equals(player.getUniqueID()) || members.contains(player.getUniqueID());
    }

    private String membersToString() {
        StringBuilder builder = new StringBuilder();

        if (members.isEmpty()) {
            return "";
        }

        for (UUID member : members) {
            builder.append(member.toString());
            builder.append(",");
        }

        builder.deleteCharAt(builder.length() - 1);

        return builder.toString();
    }
}
