package com.ipixelmon.landcontrol.regions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by colby on 1/8/2017.
 */
public class Region implements Comparable<Region> {

    protected UUID id, owner;
    private BlockPos min, max;
    private Set<SubRegion> subRegions = new TreeSet<>();
    private Set<UUID> members = new TreeSet<>();
    private Map<EnumRegionProperty, Boolean> properties = Maps.newHashMap();
    private String enterMsg, leaveMsg;

    public String ownerNameClient;
    public Map<UUID, String> membersMapClient = Maps.newHashMap();

    public Region(UUID id) {
        this.id = id;

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

                for(EnumRegionProperty propertyType : EnumRegionProperty.values())
                    properties.put(propertyType, result.getBoolean(propertyType.name()));
            }

            /**
             * Init SubRegions
             */
            ResultSet subRegionResult = getResultSubRegions();
            while (subRegionResult.next()) {
                subRegions.add(new SubRegion(id, UUID.fromString(subRegionResult.getString("id"))));
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
        return enterMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
        setViaMySQL("leaveMsg", leaveMsg);
    }

    public String getLeaveMsg() {
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
        for(UUID uuid : members) membersArray.add(uuid.toString());
        setViaMySQL("members", ArrayUtil.toString(membersArray.toArray(new String[membersArray.size()])));
    }

    public void removeMember(UUID id) {
        members.remove(id);
        List<String> membersArray = Lists.newArrayList();
        for(UUID uuid : members) membersArray.add(uuid.toString());
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
        return AxisAlignedBB.fromBounds(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public BlockPos getMin() {
        return min;
    }

    public BlockPos getMax() {
        return max;
    }

    public UUID getID() {
        return id;
    }

    protected ResultSet getResult() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class,
                new SelectionForm("Regions").where("id", id.toString()));

        return result;
    }

    private ResultSet getResultSubRegions() {
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

        if (o.getBounds().intersectsWith(getBounds())) return 0;

        return -999;
    }

    public void toBytes(ByteBuf buf) {
        // write all properties
        for(EnumRegionProperty property : EnumRegionProperty.values()) {
            buf.writeByte(property.ordinal());
            buf.writeBoolean(getProperty(property));
        }

        // write id, owner, and owner's name
        ByteBufUtils.writeUTF8String(buf, id.toString());
        ByteBufUtils.writeUTF8String(buf, owner.toString());
        ByteBufUtils.writeUTF8String(buf, UUIDManager.getPlayerName(owner));

        // write min and max
        buf.writeInt(min.getX());
        buf.writeInt(min.getY());
        buf.writeInt(min.getZ());
        buf.writeInt(max.getX());
        buf.writeInt(max.getY());
        buf.writeInt(max.getZ());

        // write members
        buf.writeInt(members.size());
        for(UUID member : members) {
            ByteBufUtils.writeUTF8String(buf, member.toString());
            ByteBufUtils.writeUTF8String(buf, UUIDManager.getPlayerName(member));
        }

        // write enterMsg and leaveMsg
        ByteBufUtils.writeUTF8String(buf, enterMsg);
        ByteBufUtils.writeUTF8String(buf, leaveMsg);
    }

    public static Region fromBytes(ByteBuf buf) {
        Region region = new Region();

        for(EnumRegionProperty property : EnumRegionProperty.values()) {
            region.properties.put(EnumRegionProperty.fromOrdinal(buf.readByte()), buf.readBoolean());
        }

        region.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        region.owner = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        region.ownerNameClient = ByteBufUtils.readUTF8String(buf);

        region.min = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        region.max = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        int membersSize = buf.readInt();
        for(int i = 0; i < membersSize; i++) {
            region.membersMapClient.put(UUID.fromString(ByteBufUtils.readUTF8String(buf)), ByteBufUtils.readUTF8String(buf));
        }

        region.enterMsg = ByteBufUtils.readUTF8String(buf);
        region.leaveMsg = ByteBufUtils.readUTF8String(buf);

        return region;
    }

}
