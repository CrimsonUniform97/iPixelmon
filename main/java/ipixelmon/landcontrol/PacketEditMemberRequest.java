package ipixelmon.landcontrol;

import io.netty.buffer.ByteBuf;
import ipixelmon.iPixelmon;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class PacketEditMemberRequest implements IMessage
{
    private String player;
    private UUID regionID;
    private boolean addMember;

    public PacketEditMemberRequest()
    {}

    public PacketEditMemberRequest(String player, Region region, boolean addMember)
    {
        this.player = player;
        this.regionID = region.getUUID();
        this.addMember = addMember;
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        player = ByteBufUtils.readUTF8String(buf);
        regionID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        addMember = buf.readBoolean();
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, player);
        ByteBufUtils.writeUTF8String(buf, regionID.toString());
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
                Region region = new Region(message.regionID);

                if (!region.getOwner().equals(ctx.getServerHandler().playerEntity.getUniqueID()))
                {
                    throw new Exception("You are not the owner.");
                }

                UUID playerUUID = UUIDManager.getUUID(message.player);

                if (playerUUID == null)
                {
                    throw new Exception("Could not find player.");
                }

                if (playerUUID.equals(region.getOwner()))
                {
                    throw new Exception("Can not add/remove owner.");
                }

                if (message.addMember)
                {
                    region.addMember(playerUUID);
                } else
                {
                    region.removeMember(playerUUID);
                }

                iPixelmon.network.sendTo(new PacketEditMemberResponse(UUIDManager.getPlayerName(playerUUID), message.addMember, true, message.addMember ? "Member added." : "Member removed."), ctx.getServerHandler().playerEntity);
            } catch (Exception e)
            {
                iPixelmon.network.sendTo(new PacketEditMemberResponse(message.player, message.addMember, false, e.getMessage()), ctx.getServerHandler().playerEntity);
            }
            return null;
        }
    }

}
