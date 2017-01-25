package com.ipixelmon.realestate.client;

import com.ipixelmon.BaseTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;

public class ForSaleSignTileEntity extends BaseTileEntity implements ITickable{

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
        compound.setInteger("facing", direction.ordinal());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        price = compound.getInteger("price");
        rentable = compound.getBoolean("rentable");
        rentCycleDays = compound.getInteger("rentCycleDays");
        direction = EnumDirection.values()[compound.getInteger("facing")];
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
        sync();
    }

    @Override
    public void update() {
        System.out.println(worldObj.getBlockState(getPos()).getValue(ForSaleSignStanding.SOLD));
    }
}
