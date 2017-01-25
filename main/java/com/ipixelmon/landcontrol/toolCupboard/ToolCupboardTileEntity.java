package com.ipixelmon.landcontrol.toolCupboard;

import com.ipixelmon.BaseTileEntity;
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
public class ToolCupboardTileEntity extends BaseTileEntity implements ITickable {

    private Set<UUID> access = new TreeSet<>();
    private Network network = new Network(UUID.randomUUID());
    private EnumFacing facing = EnumFacing.NORTH;


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("facing", getFacing().name());
        compound.setString("network", getNetwork().getID().toString());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        setFacing(EnumFacing.valueOf(compound.getString("facing")));
        setNetwork(new Network(UUID.fromString(compound.getString("network"))));
    }

    @Override
    public void update() {
        if (getWorld().isRemote) return;

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
        sync();
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        sync();
    }


}
