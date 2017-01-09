package com.ipixelmon.landcontrol.server.regions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.util.ArrayUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

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
}
