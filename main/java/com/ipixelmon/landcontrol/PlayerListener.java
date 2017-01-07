package com.ipixelmon.landcontrol;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.packet.PacketOpenGui;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by colby on 1/6/2017.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (event.world.getBlockState(event.pos).getBlock() == ToolCupboardBlock.instance) {
            ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) event.world.getTileEntity(event.pos);

            if (tileEntity == null) return;

            if (!tileEntity.getPlayers().keySet().contains(event.getPlayer().getUniqueID()))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        if (event.world.getBlockState(event.pos).getBlock() == ToolCupboardBlock.instance) {
            ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) event.world.getTileEntity(event.pos);

            if (tileEntity == null) return;

            tileEntity.getPlayers().put(event.entityPlayer.getUniqueID(), event.entityPlayer.getName());
            tileEntity.haveAccess.add(event.entityPlayer.getUniqueID());

            tileEntity.markDirty();
            event.world.markBlockForUpdate(event.pos);

            iPixelmon.network.sendTo(new PacketOpenGui(event.pos), (EntityPlayerMP) event.entityPlayer);
        }
    }

}
