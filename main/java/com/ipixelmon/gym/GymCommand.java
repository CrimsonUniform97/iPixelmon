package com.ipixelmon.gym;

import com.ipixelmon.landcontrol.LandControlAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colby on 1/10/2017.
 */
public class GymCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "gym";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gym create\n/gym addseat\n/gym delseat\n/gym startplate";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        Gym gym = GymAPI.Server.getGym(player.getPosition());

        if (args[0].equalsIgnoreCase("create")) {
            if (gym != null) {
                player.addChatComponentMessage(new ChatComponentText("Gym already exists here."));
                return;
            } else {
                if (LandControlAPI.Server.getRegionAt(player.worldObj, player.getPosition()) == null) {
                    player.addChatComponentMessage(new ChatComponentText("No region there."));
                    return;
                }

                MinecraftServer.getServer().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        Gym g = GymAPI.Server.createGym(player.worldObj, player.getPosition());
                        g.updateColoredBlocks();
                    }
                });
                player.addChatComponentMessage(new ChatComponentText("Gym created."));
            }
        } else if (args[0].equalsIgnoreCase("addseat")) {
            if (gym == null) {
                player.addChatComponentMessage(new ChatComponentText("Gym not found."));
                return;
            }

            if (gym.getSeats().size() >= 10) {
                player.addChatComponentMessage(new ChatComponentText("Max seats set."));
                return;
            }

            gym.addSeat(player.getPosition());
            player.addChatComponentMessage(new ChatComponentText("Seat added."));
        } else if (args[0].equalsIgnoreCase("delseat")) {
            if (gym == null) {
                player.addChatComponentMessage(new ChatComponentText("Gym not found."));
                return;
            }

            if (!gym.getSeats().contains(player.getPosition())) {
                player.addChatComponentMessage(new ChatComponentText("Seat not found."));
                return;
            }

            gym.removeSeat(player.getPosition());
            player.addChatComponentMessage(new ChatComponentText("Seat removed."));
        } else if (args[0].equalsIgnoreCase("startplate")) {
            if (gym == null) {
                player.addChatComponentMessage(new ChatComponentText("Gym not found."));
                return;
            }

            gym.setTeleportPos(player.getPosition());
            player.addChatComponentMessage(new ChatComponentText("StartBattlePlate set."));
        } else if (args[0].equalsIgnoreCase("respawn")) {
            if (gym == null) {
                player.addChatComponentMessage(new ChatComponentText("Gym not found."));
                return;
            }

            gym.reloadLivingEntities();
            player.addChatComponentMessage(new ChatComponentText("Spawned."));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
