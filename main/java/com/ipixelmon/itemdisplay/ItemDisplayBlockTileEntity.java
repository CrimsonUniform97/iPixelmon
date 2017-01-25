package com.ipixelmon.itemdisplay;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class ItemDisplayBlockTileEntity extends TileEntity {

    private ItemStack itemStack = new ItemStack(Items.FEATHER);

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("item", itemStack.writeToNBT(new NBTTagCompound()));
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("item"));
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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public EnumFacing getDirection() {
        return getWorld().getBlockState(getPos()).getValue(ItemDisplayBlock.facing);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        markDirty();
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()),
                    getWorld().getBlockState(getPos()), 2);
    }
}
