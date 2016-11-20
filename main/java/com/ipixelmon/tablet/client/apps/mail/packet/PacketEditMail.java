package com.ipixelmon.tablet.client.apps.mail.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.mail.MailObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colby on 11/20/2016.
 */
public class PacketEditMail implements IMessage {

    private UUID mailID;
    private boolean read, delete;

    public PacketEditMail(){}

    public PacketEditMail(UUID mailID, boolean read, boolean delete){
        this.read = read;
        this.delete = delete;
        this.mailID = mailID;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        read = buf.readBoolean();
        delete = buf.readBoolean();
        mailID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(read);
        buf.writeBoolean(delete);
        ByteBufUtils.writeUTF8String(buf, mailID.toString());
    }

    public static class Handler implements IMessageHandler<PacketEditMail, IMessage> {

        @Override
        public IMessage onMessage(PacketEditMail message, MessageContext ctx) {

            if(message.delete) {
                iPixelmon.mysql.delete(Tablet.class, new DeleteForm("Mail").add("mailID", message.mailID.toString()));
            } else if (message.read) {
                iPixelmon.mysql.update(Tablet.class, new UpdateForm("Mail").set("hasRead", true).where("mailID", message.mailID.toString()));
            }

            return null;
        }

    }

}
