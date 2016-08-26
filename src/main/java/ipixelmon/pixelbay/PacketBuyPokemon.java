package ipixelmon.pixelbay;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketBuyPokemon implements IMessage {

    public PacketBuyPokemon() {
    }

    private PixelmonData pixelmonData;
    private UUID seller;
    private long price;

    public PacketBuyPokemon(PixelmonData pixelmonData, UUID seller, long price) {
        this.pixelmonData = pixelmonData;
        this.seller = seller;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pixelmonData = new PixelmonData();
        pixelmonData.decodeInto(buf);
        seller = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        price = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        pixelmonData.encodeInto(buf);
        ByteBufUtils.writeUTF8String(buf, seller.toString());
        buf.writeLong(price);
    }

    public static class Handler implements IMessageHandler<PacketBuyPokemon, IMessage> {

        @Override
        public IMessage onMessage(PacketBuyPokemon message, MessageContext ctx) {
            return null;
        }
    }
}
