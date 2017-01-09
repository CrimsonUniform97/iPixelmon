package com.ipixelmon.landcontrol.server.regions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.landcontrol.client.gui.RegionGui;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/8/2017.
 */
public class PacketOpenRegionGui implements IMessage {

    private Map<EnumRegionProperty, Boolean> properties = Maps.newHashMap();
    private Map<UUID, String> members = Maps.newHashMap();
    private BlockPos min, max;
    private String owner;
    private UUID regionID;
    private boolean isSubRegion;

    public PacketOpenRegionGui() {
    }

    public PacketOpenRegionGui(Region region) {
        regionID = region.getID();
        properties = region.getProperties();
        min = new BlockPos(region.getBounds().minX, region.getBounds().minY, region.getBounds().minZ);
        max = new BlockPos(region.getBounds().maxX, region.getBounds().maxY, region.getBounds().maxZ);
        owner = UUIDManager.getPlayerName(region.getOwner());
        isSubRegion = region instanceof SubRegion;

        for (UUID id : region.getMembers())
            members.put(id, UUIDManager.getPlayerName(id));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();

        for (int i = 0; i < size; i++) properties.put(EnumRegionProperty.values()[buf.readInt()], buf.readBoolean());

        min = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        max = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        owner = ByteBufUtils.readUTF8String(buf);
        regionID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        isSubRegion = buf.readBoolean();

        String[] array = ArrayUtil.fromString(ByteBufUtils.readUTF8String(buf));
        for (String s : array) {
            if (!s.isEmpty())
                members.put(UUID.fromString(s.split(";")[0]), s.split(";")[1]);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(properties.size());
        for (EnumRegionProperty property : properties.keySet()) {
            buf.writeInt(property.ordinal());
            buf.writeBoolean(properties.get(property));
        }

        buf.writeInt(min.getX());
        buf.writeInt(min.getY());
        buf.writeInt(min.getZ());

        buf.writeInt(max.getX());
        buf.writeInt(max.getY());
        buf.writeInt(max.getZ());

        ByteBufUtils.writeUTF8String(buf, owner);
        ByteBufUtils.writeUTF8String(buf, regionID.toString());
        buf.writeBoolean(isSubRegion);

        List<String> membersArray = Lists.newArrayList();
        for (UUID member : members.keySet()) membersArray.add(member.toString() + ";" + members.get(member));

        ByteBufUtils.writeUTF8String(buf, ArrayUtil.toString(membersArray.toArray(new String[membersArray.size()])));
    }

    public static class Handler implements IMessageHandler<PacketOpenRegionGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenRegionGui message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void onMessage(PacketOpenRegionGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new RegionGui(message.regionID, message.isSubRegion, message.owner, message.min,
                            message.max, message.properties, message.members));
                }
            });
        }

    }

}
