package ipixelmon.landcontrol;

import io.netty.buffer.ByteBuf;
import ipixelmon.iPixelmon;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class PacketEditMemberRequest implements IMessage
{
    private String player;
    private Region region;
    private boolean addMember;

    public PacketEditMemberRequest()
    {}

    public PacketEditMemberRequest(String player, Region region, boolean addMember)
    {
        this.player = player;
        this.region = region;
        this.addMember = addMember;
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
            region = Region.getRegionAt(world, new BlockPos(x, 50, z), Side.SERVER);
        } catch (Exception e)
        {
        }

        addMember = buf.readBoolean();
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, player);
        ByteBufUtils.writeUTF8String(buf, region.getWorld());
        buf.writeInt(region.getMin().getX());
        buf.writeInt(region.getMin().getZ());
        buf.writeBoolean(addMember);
    }

    public static class Handler implements IMessageHandler<PacketEditMemberRequest, IMessage>
    {

        @Override
        public IMessage onMessage(final PacketEditMemberRequest message, final MessageContext ctx)
        {
            if (ctx.getServerHandler().playerEntity == null)
            {
                return null;
            }

            try
            {
                if (message.region == null)
                {
                    throw new Exception("Region could not be found.");
                }

                if (!message.region.getOwner().equals(ctx.getServerHandler().playerEntity.getUniqueID()))
                {
                    throw new Exception("You are not the owner.");
                }

                UUID playerUUID = UUIDManager.getUUID(message.player);

                if (playerUUID == null)
                {
                    throw new Exception("Could not find player.");
                }

                if (playerUUID.equals(message.region.getOwner()))
                {
                    throw new Exception("Can not add/remove owner.");
                }

                if (message.addMember)
                {
                    message.region.addMember(playerUUID);
                } else
                {
                    message.region.removeMember(playerUUID);
                }

                iPixelmon.network.sendTo(new PacketEditMemberResponse(UUIDManager.getPlayerName(playerUUID), message.region, message.addMember, true, message.addMember ? "Member added." : "Member removed."), ctx.getServerHandler().playerEntity);
            } catch (Exception e)
            {
                iPixelmon.network.sendTo(new PacketEditMemberResponse(message.player, message.region, message.addMember, false, e.getMessage()), ctx.getServerHandler().playerEntity);
            }
            return null;
        }
    }

}
