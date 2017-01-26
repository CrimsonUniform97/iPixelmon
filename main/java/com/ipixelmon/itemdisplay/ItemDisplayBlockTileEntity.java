package com.ipixelmon.itemdisplay;

import com.ipixelmon.BaseTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class ItemDisplayBlockTileEntity extends BaseTileEntity {

    private ItemStack itemStack = new ItemStack(Items.FEATHER);
    private int scale = 1;
    private double xOffset = 0, yOffset = 0, zOffset = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("item", itemStack.writeToNBT(new NBTTagCompound()));
        compound.setInteger("scale", scale);
        compound.setDouble("xOffset", xOffset);
        compound.setDouble("yOffset", yOffset);
        compound.setDouble("zOffset", zOffset);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("item"));
        scale = compound.getInteger("scale");
        xOffset = compound.getDouble("xOffset");
        yOffset = compound.getDouble("yOffset");
        zOffset = compound.getDouble("zOffset");
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public EnumFacing getDirection() {
        return getWorld().getBlockState(getPos()).getValue(ItemDisplayBlock.facing);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        sync();
    }

    public void setScale(int scale) {
        this.scale = scale;
        sync();
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
        sync();
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
        sync();
    }

    public void setzOffset(double zOffset) {
        this.zOffset = zOffset;
        sync();
    }

    public int getScale() {
        return scale;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public double getzOffset() {
        return zOffset;
    }
}
