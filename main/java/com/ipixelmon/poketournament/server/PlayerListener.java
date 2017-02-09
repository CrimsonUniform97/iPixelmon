package com.ipixelmon.poketournament.server;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.Team;
import com.ipixelmon.poketournament.TournamentAPI;
import com.ipixelmon.util.DateUtil;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Calendar;
import java.util.List;

public class PlayerListener {

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getWorld().getBlockState(event.getPos()) == null) return;

        if (event.getWorld().getBlockState(event.getPos()) == null) return;

        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        if (block != Blocks.WALL_SIGN && block != Blocks.STANDING_SIGN) return;

        TileEntitySign tileEntity = (TileEntitySign) event.getWorld().getTileEntity(event.getPos());

        boolean isJoinSign = false;

        for (ITextComponent textComponent : tileEntity.signText) {
            if (textComponent != null) {
                if (textComponent.getUnformattedText() != null) {
                    if (textComponent.getUnformattedText().contains("[Join]")) {
                        isJoinSign = true;
                        break;
                    }
                }
            }
        }

        if (!isJoinSign) return;

        Arena arena = TournamentAPI.Server.getArena(LandControlAPI.Server.getRegionAt(event.getWorld(), tileEntity.getPos()));

        if (arena == null) return;

        if (arena.isStarted()) {
            event.getEntityPlayer().addChatComponentMessage(new TextComponentString("Game has already started."));
            return;
        }

        boolean playerInGame = false;

        for (Team team : arena.getTournament().getTeams()) {
            for (EntityPlayerMP player : team.players) {
                if (player.getUniqueID().equals(event.getEntityPlayer().getUniqueID())) {
                    playerInGame = true;
                    break;
                }
            }
        }

        if (arena.getTournament().getTeams().isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 3);
            arena.setStartTime(calendar.getTime());
        }

        iPixelmon.network.sendTo(new PacketOpenTournamentGui(arena.getRegion().getID(), arena.getTournament(), playerInGame), (EntityPlayerMP) event.getEntityPlayer());
    }

    private List<Arena> sentMessage = Lists.newArrayList();

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {

        for (Arena arena : TournamentAPI.Server.ARENAS) {
            if (arena.getStartTime() != null && arena.getTournament() != null && arena.getTournament().getTeams() != null
                    && !arena.getTournament().getTeams().isEmpty()) {


                if (DateUtil.getDifferenceInSeconds(Calendar.getInstance().getTime(), arena.getStartTime()) % 30 == 0) {

                    if (!sentMessage.contains(arena)) {
                        sentMessage.add(arena);
                        // start game
                        if (DateUtil.getDifferenceInSeconds(Calendar.getInstance().getTime(), arena.getStartTime()) <= 0) {
                            arena.start();
                        } else {
                            for (Team team : arena.getTournament().getTeams()) {
                                for (EntityPlayerMP player : team.players) {
                                    if (player != null) {
                                        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
                                        ICommandManager commandManager = server.getCommandManager();

                                        commandManager.executeCommand(server, "/title " + player.getName() + " subtitle {\"text\":\"" + DateUtil.getDifferenceFormatted(arena.getStartTime()) + "\",\"italic\":\"true\",\"color\":\"yellow\"}");
                                        commandManager.executeCommand(server, "/title " + player.getName() + " title {\"text\":\"Tournament Starting in\",\"bold\":\"true\",\"italic\":\"true\",\"color\":\"red\"}");
                                        commandManager.executeCommand(server, "/title " + player.getName() + " times 40 120 60");
                                    }
                                }
                            }
                        }
                    }

                } else {
                    sentMessage.remove(arena);
                }
            }

        }
    }

}
