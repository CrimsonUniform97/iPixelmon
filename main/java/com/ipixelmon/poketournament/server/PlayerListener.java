package com.ipixelmon.poketournament.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.Team;
import com.ipixelmon.poketournament.TournamentAPI;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerListener {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.getWorld().getBlockState(event.getPos()) == null) return;

        if(event.getWorld().getBlockState(event.getPos()) == null) return;

        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        if(block != Blocks.WALL_SIGN && block != Blocks.STANDING_SIGN) return;

        TileEntitySign tileEntity = (TileEntitySign) event.getWorld().getTileEntity(event.getPos());

        boolean isJoinSign = false;

        for(ITextComponent textComponent : tileEntity.signText) {
            if(textComponent != null) {
                if(textComponent.getUnformattedText() != null) {
                    if(textComponent.getUnformattedText().contains("[Join]")) {
                        isJoinSign = true;
                        break;
                    }
                }
            }
        }

        if(!isJoinSign) return;

        Arena arena = TournamentAPI.Server.getArena(LandControlAPI.Server.getRegionAt(event.getWorld(), tileEntity.getPos()));

        if(arena == null) return;

        if(arena.isStarted()) {
            event.getEntityPlayer().addChatComponentMessage(new TextComponentString("Game has already started."));
            return;
        }

        boolean playerInGame = false;

        for(Team team : arena.getTournament().getTeams()) {
            for(EntityPlayerMP player : team.players) {
                if(player.getUniqueID().equals(event.getEntityPlayer().getUniqueID())) {
                    playerInGame = true;
                    break;
                }
            }
        }

        iPixelmon.network.sendTo(new PacketOpenTournamentGui(arena.getRegion().getID(), arena.getTournament(), playerInGame), (EntityPlayerMP) event.getEntityPlayer());
    }

}
