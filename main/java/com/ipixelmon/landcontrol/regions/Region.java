package com.ipixelmon.landcontrol.regions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by colby on 1/8/2017.
 */
public class Region implements Comparable<Region> {

    public List<EntityPlayer> playersInside = Lists.newArrayList();

    private String world;
    protected UUID id, owner;
    private BlockPos min, max;
    private Set<SubRegion> subRegions = new TreeSet<>();
    private Set<UUID> members = new TreeSet<>();
    private Map<EnumRegionProperty, Boolean> properties = Maps.newHashMap();
    private String enterMsg = "null", leaveMsg = "null";

    public String ownerNameClient;
    public Map<UUID, String> membersMapClient = Maps.newHashMap();

    public Region(UUID id) {
        this.id = id;
    }

    public void init() {
        ResultSet result = getResult();

        try {

            /**
             * Init min, max, and trusted players
             */
            if (result.next()) {
                String[] minArray = ArrayUtil.fromString(result.getString("min"));
                String[] maxArray = ArrayUtil.fromString(result.getString("max"));

                min = new BlockPos(Integer.parseInt(minArray[0]), Integer.parseInt(minArray[1]), Integer.parseInt(minArray[2]));
                max = new BlockPos(Integer.parseInt(maxArray[0]), Integer.parseInt(maxArray[1]), Integer.parseInt(maxArray[2]));

                String[] playerArray = ArrayUtil.fromString(result.getString("members"));
                for (String s : playerArray) {
                    if (!s.isEmpty()) members.add(UUID.fromString(s));
                }

                owner = UUID.fromString(result.getString("owner"));
                world = result.getString("world");

                for (EnumRegionProperty propertyType : EnumRegionProperty.values())
                    properties.put(propertyType, result.getBoolean(propertyType.name()));

                enterMsg = result.getString("enterMsg");
                leaveMsg = result.getString("leaveMsg");
            }

            /**
             * Init SubRegions
             */
            ResultSet subRegionResult = getResultSubRegions();

            if (subRegionResult != null) {
                SubRegion subRegion;
                while (subRegionResult.next()) {
                    subRegions.add(subRegion = new SubRegion(id, UUID.fromString(subRegionResult.getString("id"))));
                    subRegion.init();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Region() {

    }

    public boolean getProperty(EnumRegionProperty property) {
        return properties.get(property);
    }

    public void setProperty(EnumRegionProperty property, boolean value) {
        properties.put(property, value);
        setViaMySQL(property.name(), String.valueOf(value));
    }

    public Map<EnumRegionProperty, Boolean> getProperties() {
        return properties;
    }

    public void setEnterMsg(String enterMsg) {
        this.enterMsg = enterMsg;
        setViaMySQL("enterMsg", enterMsg);
    }

    public String getEnterMsg() {
        if (enterMsg == null || enterMsg.isEmpty()) return null;
        return enterMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
        setViaMySQL("leaveMsg", leaveMsg);
    }

    public String getLeaveMsg() {
        if (leaveMsg == null || leaveMsg.isEmpty()) return null;
        return leaveMsg;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        setViaMySQL("owner", owner.toString());
    }

    public void addMember(UUID id) {
        members.add(id);
        List<String> membersArray = Lists.newArrayList();
        for (UUID uuid : members) membersArray.add(uuid.toString());
        setViaMySQL("members", ArrayUtil.toString(membersArray.toArray(new String[membersArray.size()])));
    }

    public void removeMember(UUID id) {
        members.remove(id);
        List<String> membersArray = Lists.newArrayList();
        for (UUID uuid : members) membersArray.add(uuid.toString());
        setViaMySQL("members", ArrayUtil.toString(membersArray.toArray(new String[membersArray.size()])));
    }

    public boolean isMember(UUID id) {
        return members.contains(id);
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public Set<SubRegion> getSubRegions() {
        return subRegions;
    }

    public AxisAlignedBB getBounds() {
        return new AxisAlignedBB(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public BlockPos getCenter() {
        Vector minVec = new Vector(min.getX(), min.getY(), min.getZ());
        Vector maxVec = new Vector(max.getX(), max.getY(), max.getZ());

        com.sk89q.worldedit.regions.Region r = new CuboidRegion(minVec, maxVec);
        Vector center = r.getCenter();
        return new BlockPos(center.getBlockX(), center.getBlockY(), center.getBlockZ());
    }

    public BlockPos getMin() {
        return min;
    }

    public BlockPos getMax() {
        return max;
    }

    public void setWorld(World world) {
        this.world = world.getWorldInfo().getWorldName();
        setViaMySQL("world", world.getWorldInfo().getWorldName());
    }

    @SideOnly(Side.SERVER)
    public WorldServer getWorld() {
        for (World w : iPixelmon.proxy.getDefaultWorld().getMinecraftServer().worldServers) {
            if (w.getWorldInfo().getWorldName().equals(world)) return (WorldServer) w;
        }
        return null;
    }

    public String getWorldName() {
        return world;
    }

    public UUID getID() {
        return id;
    }

    public SubRegion getSubRegionAt(BlockPos pos) {
        for (SubRegion subRegion : getSubRegions()) {
            if (subRegion.getBounds().isVecInside(new Vec3d(pos.getX(), pos.getY(), pos.getZ()))) return subRegion;
        }
        return null;
    }

    protected ResultSet getResult() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class,
                new SelectionForm("Regions").where("id", id.toString()));

        return result;
    }

    protected ResultSet getResultSubRegions() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class,
                new SelectionForm("SubRegions").where("parentID", id.toString()));

        return result;
    }

    protected void setViaMySQL(String column, String value) {
        iPixelmon.mysql.update(LandControl.class, new UpdateForm("Regions").set(column, value).where("id", id.toString()));
    }

    @Override
    public int compareTo(Region o) {
        if (o.id.equals(id)) return 0;

        if (o.world.equalsIgnoreCase(world) && o.getBounds().intersectsWith(getBounds())) return 0;

        return -999;
    }

    public void toBytes(ByteBuf buf) {
        // write all properties
        for (EnumRegionProperty property : EnumRegionProperty.values()) {
            buf.writeByte(property.ordinal());
            buf.writeBoolean(getProperty(property));
        }

        // write id, owner, and owner's name
        ByteBufUtils.writeUTF8String(buf, id.toString());
        ByteBufUtils.writeUTF8String(buf, owner.toString());
        ByteBufUtils.writeUTF8String(buf, UUIDManager.getPlayerName(owner));
        ByteBufUtils.writeUTF8String(buf, getWorldName());

        // write min and max
        buf.writeInt(min.getX());
        buf.writeInt(min.getY());
        buf.writeInt(min.getZ());
        buf.writeInt(max.getX());
        buf.writeInt(max.getY());
        buf.writeInt(max.getZ());

        // write members
        buf.writeInt(members.size());
        for (UUID member : members) {
            ByteBufUtils.writeUTF8String(buf, member.toString());
            ByteBufUtils.writeUTF8String(buf, UUIDManager.getPlayerName(member));
        }

        // write enterMsg and leaveMsg
        ByteBufUtils.writeUTF8String(buf, enterMsg == null ? "" : enterMsg);
        ByteBufUtils.writeUTF8String(buf, leaveMsg == null ? "" : leaveMsg);
    }

    public static Region fromBytes(ByteBuf buf) {
        Region region = new Region();

        for (EnumRegionProperty property : EnumRegionProperty.values()) {
            region.properties.put(EnumRegionProperty.fromOrdinal(buf.readByte()), buf.readBoolean());
        }

        region.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        region.owner = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        region.ownerNameClient = ByteBufUtils.readUTF8String(buf);
        region.world = ByteBufUtils.readUTF8String(buf);

        region.min = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        region.max = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        int membersSize = buf.readInt();
        for (int i = 0; i < membersSize; i++) {
            region.membersMapClient.put(UUID.fromString(ByteBufUtils.readUTF8String(buf)), ByteBufUtils.readUTF8String(buf));
        }

        region.enterMsg = ByteBufUtils.readUTF8String(buf);
        region.leaveMsg = ByteBufUtils.readUTF8String(buf);

        return region;
    }

    public void delete() {
        iPixelmon.mysql.delete(LandControl.class, new DeleteForm("Regions").add("id", id.toString()));
        iPixelmon.mysql.delete(LandControl.class, new DeleteForm("SubRegions").add("parentID", id.toString()));
        LandControlAPI.Server.regions.remove(this);
    }

}
