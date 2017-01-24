package com.ipixelmon.realestate.client;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;

import javax.annotation.Nullable;

public class ForSaleSignTileEntity extends TileEntity {

    private int price;
    private boolean rentable;
    private int rentCycleDays;
    private EnumDirection direction = EnumDirection.NORTH;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("price", price);
        compound.setBoolean("rentable", rentable);
        compound.setInteger("rentCycleDays", rentCycleDays);
        compound.setInteger("direction", direction.ordinal());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        price = compound.getInteger("price");
        rentable = compound.getBoolean("rentable");
        rentCycleDays = compound.getInteger("rentCycleDays");
        direction = EnumDirection.values()[compound.getInteger("direction")];
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, getBlockMetadata(), this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public int getPrice() {
        return price;
    }

    public boolean isRentable() {
        return rentable;
    }

    public int getRentCycleDays() {
        return rentCycleDays;
    }

    public EnumDirection getDirection() {
        return direction;
    }

    public void setDirection(EnumDirection direction) {
        this.direction = direction;
        markDirty();
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()),
                    getWorld().getBlockState(getPos()), 2);
    }
}
