package com.ipixelmon.tablet.app.pixelbay.packet.buy;

import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 1/3/2017.
 */
public class PacketRequestPage implements IMessage {

    private int page;
    private String criteria;
    private boolean items;

    public PacketRequestPage() {
    }

    public PacketRequestPage(int page, String criteria, boolean items) {
        this.page = page;
        this.criteria = criteria;
        this.items = items;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        page = buf.readInt();
        criteria = ByteBufUtils.readUTF8String(buf);
        items = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(page);
        ByteBufUtils.writeUTF8String(buf, criteria);
        buf.writeBoolean(items);
    }

    public static class Handler implements IMessageHandler<PacketRequestPage, IMessage> {

        @Override
        public IMessage onMessage(PacketRequestPage message, MessageContext ctx) {
            iPixelmon.network.sendTo(new PacketResultsToClient(message.page, message.criteria, message.items),
                    ctx.getServerHandler().playerEntity);
            return null;
        }

    }
}
