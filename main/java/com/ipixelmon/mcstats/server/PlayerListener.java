package com.ipixelmon.mcstats.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mcstats.GatherType;
import com.ipixelmon.mcstats.McStatsAPI;
import com.ipixelmon.mcstats.McStatsMod;
import com.ipixelmon.mcstats.PacketBrokeBlock;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
        IBlockState blockState = event.getWorld().getBlockState(event.getPos());
        int exp = McStatsAPI.Server.expValueList.getEXP(blockState);

        if (exp == 0) return;

        McStatsAPI.Server.giveEXP(player.getUniqueID(), exp, McStatsAPI.Server.expValueList.getGatherType(blockState));
        iPixelmon.network.sendTo(new PacketBrokeBlock(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), exp), player);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        // TODO: Check if it's only right click
        try {
            McStatsAPI.Mining.applySuperBreaker((EntityPlayerMP) event.getEntityPlayer());
        } catch (Exception e) {
            event.getEntityPlayer().addChatComponentMessage(new TextComponentString(e.getMessage()));
        }
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(McStatsMod.class,
                new SelectionForm("STATS").where("player", event.player.getUniqueID().toString()));

        try {
            if (!result.next()) {
                InsertForm insertForm = new InsertForm("STATS").add("player", event.player.getUniqueID().toString());

                for (GatherType gatherType : GatherType.values()) insertForm.add(gatherType.name(), 0);

                iPixelmon.mysql.insert(McStatsMod.class, insertForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (GatherType gatherType : GatherType.values())
            McStatsAPI.Server.updatePlayer((EntityPlayerMP) event.player, gatherType);
    }

    // TODO: test
    @SubscribeEvent
    public void onTick(final TickEvent.PlayerTickEvent event) {
        if (McStatsAPI.Mining.getSuperBreakerTimeLeft(event.player.getUniqueID()) == 0) {
            McStatsAPI.Mining.removeSuperBreakerFromItem(event.player.getUniqueID());
        }
    }

}
