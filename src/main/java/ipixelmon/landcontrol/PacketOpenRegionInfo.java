package ipixelmon.landcontrol;

import io.netty.buffer.ByteBuf;
import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.client.GuiRegionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenRegionInfo implements IMessage
{

    private String world;
    private BlockPos pos;

    public PacketOpenRegionInfo()
    {
    }

    public PacketOpenRegionInfo(Region region)
    {
        world = region.getWorld();
        pos = region.getMin();
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        world = ByteBufUtils.readUTF8String(buf);
        int x = buf.readInt();
        int z = buf.readInt();
        pos = new BlockPos(x, 50, z);
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, world);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketOpenRegionInfo, IMessage>
    {

        @Override
        public IMessage onMessage(final PacketOpenRegionInfo message, final MessageContext ctx)
        {
            openGui(message);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void openGui(final PacketOpenRegionInfo message)
    {
        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiRegionInfo(Region.getRegionAt(message.world, message.pos, Side.CLIENT)));
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
