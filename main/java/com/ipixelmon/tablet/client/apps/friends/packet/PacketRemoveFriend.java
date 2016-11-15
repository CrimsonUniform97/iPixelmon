package com.ipixelmon.tablet.client.apps.friends.packet;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.Friend;
import com.ipixelmon.tablet.client.apps.friends.FriendsAPI;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Set;
import java.util.UUID;

/**
 * Created by colby on 11/15/2016.
 */
public class PacketRemoveFriend implements IMessage {

    public PacketRemoveFriend() {
    }

    private UUID uuid;

    public PacketRemoveFriend(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
    }

    public static class Handler implements IMessageHandler<PacketRemoveFriend, IMessage> {

        @Override
        public IMessage onMessage(PacketRemoveFriend message, MessageContext ctx) {
            final EntityPlayerMP friend = PlayerUtil.getPlayer(message.uuid);

            Set<UUID> friends = FriendsAPI.getFriendsUUIDOnly(ctx.getServerHandler().playerEntity.getUniqueID());
            if(!friends.contains(message.uuid)) {
                return null;
            }

            friends.remove(message.uuid);
            String s = "";
            for(UUID uuid : friends) s += uuid.toString() + ",";

            iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends").set("friends", s).where("player",
                    ctx.getServerHandler().playerEntity.getUniqueID().toString()));

            friends = FriendsAPI.getFriendsUUIDOnly(message.uuid);
            friends.remove(ctx.getServerHandler().playerEntity.getUniqueID());
            s = "";
            for(UUID uuid : friends) s += uuid.toString() + ",";

            iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends").set("friends", s).where("player", message.uuid.toString()));

            iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.REMOVE,
                    message.uuid.toString() + "," + UUIDManager.getPlayerName(message.uuid)), ctx.getServerHandler().playerEntity);

            if(friend != null)
                iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.REMOVE,
                        ctx.getServerHandler().playerEntity.getUniqueID().toString() + ","
                                + ctx.getServerHandler().playerEntity.getName()), friend);

            return null;
        }

    }

}
