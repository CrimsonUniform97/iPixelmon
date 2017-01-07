package com.ipixelmon.landcontrol;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Created by colby on 1/6/2017.
 */
public class ToolCupboardTileEntity extends TileEntity {

    public Set<UUID> haveAccess = new TreeSet<>();

    private EnumFacing FACING;
    private Map<UUID, String> players = Maps.newHashMap();

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writePacket(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readPacket(compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readPacket(pkt.getNbtCompound());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writePacket(tagCompound);
        return new S35PacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
    }

    private void readPacket(NBTTagCompound compound) {
        FACING = EnumFacing.getFront((int) compound.getByte("FACING"));

        if (FACING.getAxis() == EnumFacing.Axis.Y) FACING = EnumFacing.NORTH;

        String[] array = ArrayUtil.fromString(compound.getString("players"));

        String[] data;
        for(String s : array) {
            if(s != null && !s.isEmpty() && s.contains(";")) {
                data = s.split(";");
                players.put(UUID.fromString(data[0]), data[1]);
            }
        }
    }

    private void writePacket(NBTTagCompound compound) {
        compound.setByte("FACING", FACING == null ? (byte) EnumFacing.EAST.getIndex() : (byte) FACING.getIndex());

        List<String> players = Lists.newArrayList();

        for(UUID uuid : this.players.keySet()) players.add(uuid.toString() + ";" + UUIDManager.getPlayerName(uuid));

        compound.setString("players", ArrayUtil.toString(players.toArray(new String[players.size()])));
    }

    public EnumFacing getFacing() {
        return FACING != null ? FACING : EnumFacing.SOUTH;
    }

    public void setFacing(EnumFacing facing) {
        this.FACING = facing;
    }

    public Map<UUID, String> getPlayers() {
        return getTileEntity().players;
    }

    private ToolCupboardTileEntity getTileEntity() {
        if(ToolCupboardBlock.instance.getMetaFromState(worldObj.getBlockState(pos)) == 1) {
            return (ToolCupboardTileEntity) worldObj.getTileEntity(pos.down());
        }

        return (ToolCupboardTileEntity) worldObj.getTileEntity(pos);
    }
}
