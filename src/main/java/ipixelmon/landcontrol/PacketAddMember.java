package ipixelmon.landcontrol;

import io.netty.buffer.ByteBuf;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketAddMember implements IMessage
{

    public PacketAddMember()
    {
    }

    private String player;
    private Region region;

    public PacketAddMember(String player, Region region)
    {
        this.player = player;
        this.region = region;
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        player = ByteBufUtils.readUTF8String(buf);
        String world = ByteBufUtils.readUTF8String(buf);
        int x = buf.readInt();
        int z = buf.readInt();

        try
        {
            region = Region.getRegionAt(world, new BlockPos(x, 50, z));
        } catch (Exception e)
        {
        }
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, player);
        ByteBufUtils.writeUTF8String(buf, region.getWorld());
        buf.writeInt(region.getMin().getX());
        buf.writeInt(region.getMin().getZ());
    }

    public static class Handler implements IMessageHandler<PacketAddMember, IMessage>
    {

        @Override
        public IMessage onMessage(final PacketAddMember message, final MessageContext ctx)
        {
            if(message.region == null)
            {
                ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("Region error. Could not be found."));
                return null;
            }

            if(!message.region.getOwner().equals(ctx.getServerHandler().playerEntity.getUniqueID()))
            {
                ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("Region error. You are not the owner."));
                return null;
            }

            UUID playerUUID = UUIDManager.getUUID(message.player);

            if(playerUUID == null)
            {
                ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("Region error. Could not find player."));
                return null;
            }

            message.region.addMember(playerUUID);
            ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("Member added."));
            return null;
        }
    }
}
