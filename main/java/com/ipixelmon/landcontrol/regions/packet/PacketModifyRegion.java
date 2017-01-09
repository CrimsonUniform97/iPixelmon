package com.ipixelmon.landcontrol.regions.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.EnumRegionProperty;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colby on 1/8/2017.
 */
public class PacketModifyRegion implements IMessage {

    private String key;
    private String value;
    private UUID regionID;

    public PacketModifyRegion() {
    }

    public PacketModifyRegion(UUID regionID, String key, String value) {
        this.regionID = regionID;
        this.key = key;
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        regionID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        key = ByteBufUtils.readUTF8String(buf);
        value = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, regionID.toString());
        ByteBufUtils.writeUTF8String(buf, key);
        ByteBufUtils.writeUTF8String(buf, value);
    }

    public static class Handler implements IMessageHandler<PacketModifyRegion, IMessage> {

        @Override
        public IMessage onMessage(PacketModifyRegion message, MessageContext ctx) {
            Region region = LandControlAPI.Server.getRegion(message.regionID);

            if(region == null) return null;

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if(!region.getOwner().equals(player.getUniqueID())) return null;

            if(EnumRegionProperty.contains(message.key)) {
                region.setProperty(EnumRegionProperty.valueOf(message.key), Boolean.parseBoolean(message.value));
                return null;
            }

            if(message.key.equalsIgnoreCase("addPlayer")) {
                UUID playerID = UUIDManager.getUUID(message.value);

                if(playerID == null) {
                    iPixelmon.network.sendTo(new PacketModifyRegionResponse("Player does not exist."), player);
                    return null;
                }

                region.addMember(playerID);
            } else if (message.key.equalsIgnoreCase("removePlayer")) {
                UUID playerID = UUID.fromString(message.value);
                region.removeMember(playerID);
            } else if (message.key.equalsIgnoreCase("enterMsg")) {
                if(message.value.length() > 100) return null;
                region.setEnterMsg(message.value);
            } else if (message.key.equalsIgnoreCase("leaveMsg")) {
                if(message.value.length() > 100) return null;
                region.setLeaveMsg(message.value);
            }

            return null;
        }
    }

}
