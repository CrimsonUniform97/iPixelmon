package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import javafx.scene.control.Tab;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/5/2016.
 */
public class PacketAddFriendReq implements IMessage {

    public PacketAddFriendReq() {
    }

    private String playerName;

    public PacketAddFriendReq(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerName);
    }


    public static class Handler implements IMessageHandler<PacketAddFriendReq, IMessage> {

        @Override
        public IMessage onMessage(PacketAddFriendReq message, MessageContext ctx) {
            try {
                UUID friendUUID = UUIDManager.getUUID(message.playerName);

                ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class,
                        new SelectionForm("FriendReqs").where("player", ctx.getServerHandler().playerEntity.getUniqueID().toString())
                                .where("friend", friendUUID.toString()));

                if (result.next()) {
                    iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.PENDING, message.playerName), ctx.getServerHandler().playerEntity);
                    return null;
                }

                InsertForm insertForm = new InsertForm("FriendReqs");
                insertForm.add("player", ctx.getServerHandler().playerEntity.getUniqueID().toString());
                insertForm.add("friend", friendUUID.toString());
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                Date today = Calendar.getInstance().getTime();
                insertForm.add("sentDate", df.format(today));
                iPixelmon.mysql.insert(Tablet.class, insertForm);
                iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.SENT, message.playerName), ctx.getServerHandler().playerEntity);

                if (PlayerUtil.isPlayerOnline(friendUUID))
                    iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.REQUEST, ctx.getServerHandler().playerEntity.getName()), (EntityPlayerMP) PlayerUtil.getPlayer(friendUUID));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

}
