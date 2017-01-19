package com.ipixelmon.landcontrol.toolCupboard;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by colby on 1/7/2017.
 */
public class ToolCupboardTileEntity extends TileEntity implements ITickable {

    private Set<UUID> access = new TreeSet<>();
    private Network network = new Network(UUID.randomUUID());
    private EnumFacing facing = EnumFacing.NORTH;


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        write(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        read(compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        read(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        write(tagCompound);
        return tagCompound;
    }

    @Override
    public void update() {
        int meta = ToolCupboardBlock.instance.getMetaFromState(getWorld().getBlockState(getPos()));

        if (meta == 1) {
            if (!(getWorld().getTileEntity(getPos().down()) instanceof ToolCupboardTileEntity)) {
                getWorld().destroyBlock(getPos(), false);
            }
        } else {
            if (!(getWorld().getTileEntity(getPos().up()) instanceof ToolCupboardTileEntity)) {
                getWorld().destroyBlock(getPos(), false);
            }
        }
    }

    private void write(NBTTagCompound tagCompound) {
        tagCompound.setString("facing", getFacing().name());
        tagCompound.setString("network", getNetwork().getID().toString());
    }

    private void read(NBTTagCompound tagCompound) {
        setFacing(EnumFacing.valueOf(tagCompound.getString("facing")));
        setNetwork(new Network(UUID.fromString(tagCompound.getString("network"))));
    }

    /**
     * GETTERS
     */
    public Set<UUID> getAccessSet() {
        return access;
    }

    public Network getNetwork() {
        return network;
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public ToolCupboardTileEntity getBaseTile() {
        int meta = ToolCupboardBlock.instance.getMetaFromState(getWorld().getBlockState(getPos()));

        if (meta == 1) {
            if (getWorld().getTileEntity(getPos().down()) != null) {
                if (getWorld().getTileEntity(getPos().down()) instanceof ToolCupboardTileEntity) {
                    return (ToolCupboardTileEntity) getWorld().getTileEntity(getPos().down());
                }
            }
        }

        return this;
    }


    /**
     * SETTERS
     */
    public void setNetwork(Network network) {
        this.network = network;
        markDirty();
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 2);
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        markDirty();
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 2);
    }


}
