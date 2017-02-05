package com.ipixelmon.poketournament.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.Team;
import com.ipixelmon.poketournament.TournamentAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerListener {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.getWorld().getBlockState(event.getPos()) == null) return;
        if (event.getWorld().getBlockState(event.getPos()).getBlock() != Blocks.STONE_BUTTON) return;

        Arena arena = TournamentAPI.Server.getArena(LandControlAPI.Server.getRegionAt(event.getWorld(), event.getPos()));

        if (arena == null) return;
//
//        if (arena.isStarted()) {
//            event.getEntityPlayer().addChatComponentMessage(new TextComponentString("Tournament has already started."));
//            return;
//        }
//
//        for (Team team : arena.getTournament().getTeams()) {
//            for (EntityPlayerMP player : team.players) {
//                if (player.getUniqueID().equals(event.getEntityPlayer().getUniqueID())) {
//                    iPixelmon.network.sendTo(new PacketOpenTournamentGui(arena.getRegion().getID(), arena.getTournament(), true), (EntityPlayerMP) event.getEntityPlayer());
//                    return;
//                }
//            }
//        }

        arena.getTournament().getTeams().clear();
        arena.getTournament().setRound(1);
        arena.getTournament().getMatches().clear();

        for(int i = 1; i < 19; i++) {
            arena.getTournament().addTeam(new Team("Team" + i));
        }

        try {
            arena.getTournament().setupRounds();
        } catch (Exception e) {
            e.printStackTrace();
        }

        iPixelmon.network.sendTo(new PacketOpenTournamentGui(arena.getRegion().getID(), arena.getTournament(), true), (EntityPlayerMP) event.getEntityPlayer());
    }

}
