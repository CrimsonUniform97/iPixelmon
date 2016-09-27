package ipixelmon.landcontrol;

import io.netty.buffer.ByteBuf;
import ipixelmon.TimedMessage;
import ipixelmon.landcontrol.client.GuiRegionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketEditMemberResponse implements IMessage
{

    private String player;
    private boolean addMember;
    private boolean successful;
    private String message;

    public PacketEditMemberResponse()
    {
    }

    public PacketEditMemberResponse(String player, boolean addMember, boolean successful, String message)
    {
        this.player = player;
        this.addMember = addMember;
        this.successful = successful;
        this.message = message;
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        player = ByteBufUtils.readUTF8String(buf);
        addMember = buf.readBoolean();
        successful = buf.readBoolean();
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, player);
        buf.writeBoolean(addMember);
        buf.writeBoolean(successful);
        ByteBufUtils.writeUTF8String(buf, message);
    }

    public static class Handler implements IMessageHandler<PacketEditMemberResponse, IMessage>
    {

        @Override
        public IMessage onMessage(final PacketEditMemberResponse message, final MessageContext ctx)
        {
            updateScreen(message);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void updateScreen(final PacketEditMemberResponse message)
    {
        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                if (Minecraft.getMinecraft().currentScreen == null)
                {
                    return;
                }

                if (!(Minecraft.getMinecraft().currentScreen instanceof GuiRegionInfo))
                {
                    return;
                }

                GuiRegionInfo screen = (GuiRegionInfo) Minecraft.getMinecraft().currentScreen;
                if (message.addMember)
                {
                    screen.scrollList.playerNames.add(message.player);
                } else
                {
                    screen.scrollList.playerNames.remove(message.player);
                }
                new Thread(screen.message = new TimedMessage((message.successful ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + message.message, 3)).start();
            }
        });
    }
}
