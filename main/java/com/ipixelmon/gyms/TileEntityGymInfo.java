package com.ipixelmon.gyms;

import com.ipixelmon.gyms.Gym;
import com.ipixelmon.gyms.Gyms;
import com.ipixelmon.landcontrol.LandControl;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.UUID;

/**
 * Created by colby on 10/16/2016.
 */
public class TileEntityGymInfo extends TileEntity implements ITickable{

    private Gym gym;
    private long gymPower;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("gymRegion")) {
            try {
                gym = Gyms.getGymForClient(LandControl.getRegion(UUID.fromString(compound.getString("gymRegion"))));
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        try {
            compound.setString("gymRegion", Gyms.getGym(LandControl.getRegion(getWorld(), getPos())).getRegion().id().toString());
        } catch (Exception e) {
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if (pkt.getNbtCompound().hasKey("gymRegion")) {
            try {
                gym = Gyms.getGymForClient(LandControl.getRegion(UUID.fromString(pkt.getNbtCompound().getString("gymRegion"))));
            } catch (Exception e) {
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tagCompound = new NBTTagCompound();

        try {
            tagCompound.setString("gymRegion", Gyms.getGym(LandControl.getRegion(getWorld(), getPos())).getRegion().id().toString());
        } catch (Exception e) {
        }

        return new S35PacketUpdateTileEntity(pos, 1, tagCompound) ;
    }

    @Override
    public void onLoad() {
        if(!worldObj.isRemote) {
            try {
                gym = Gyms.getGym(LandControl.getRegion(getWorld(), getPos()));
                gymPower = gym.getPower();
                markForUpdate();
            } catch (Exception e) {
            }
        }
    }

    public Gym getGym() {
        return gym;
    }

    public void markForUpdate() {
        worldObj.markBlockForUpdate(pos);
    }

    @Override
    public void update() {
        if(!getWorld().isRemote) {
            if (gym != null) {
                try {
                    if (gymPower != gym.getPower()) {
                        gymPower = gym.getPower();
                        markDirty();
                        markForUpdate();
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
