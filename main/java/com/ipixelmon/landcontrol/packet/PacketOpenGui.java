package com.ipixelmon.landcontrol.packet;

import com.google.common.collect.Maps;
import com.ipixelmon.landcontrol.client.gui.ToolCupboardGui;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/7/2017.
 */
public class PacketOpenGui implements IMessage {

    private BlockPos pos;
    private Map<UUID, String> players;
    private boolean hasNetwork;

    public PacketOpenGui() {
    }

    public PacketOpenGui(ToolCupboardTileEntity tileEntity, Map<UUID, String> players) {
        this.hasNetwork = tileEntity.getBaseTile().getNetwork().exists();
        this.pos = tileEntity.getBaseTile().getPos();
        this.players = players;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        hasNetwork = buf.readBoolean();
        players = Maps.newHashMap();

        int size = buf.readInt();

        String[] data;
        for(int i = 0; i < size; i++) {
            data = ByteBufUtils.readUTF8String(buf).split(",");
            players.put(UUID.fromString(data[0]), data[1]);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(hasNetwork);

        buf.writeInt(players.size());

        for(UUID id : players.keySet())
            ByteBufUtils.writeUTF8String(buf, id.toString() + "," + players.get(id));
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void onMessage(PacketOpenGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = Minecraft.getMinecraft().theWorld;
                    ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) world.getTileEntity(message.pos);

                    if(tileEntity != null)
                        Minecraft.getMinecraft().displayGuiScreen(new ToolCupboardGui(tileEntity,
                                message.hasNetwork && message.players.containsKey(Minecraft.getMinecraft().thePlayer.getUniqueID()),
                                message.players));
                }
            });

        }

    }
}
