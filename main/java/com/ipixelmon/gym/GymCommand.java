package com.ipixelmon.gym;

import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.permission.PermissionAPI;
import com.ipixelmon.permission.PermissionMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        Gym gym = GymAPI.Server.getGym(player.getPosition());

        if (args[0].equalsIgnoreCase("create")) {
            if (gym != null) {
                player.addChatComponentMessage(new TextComponentString("Gym already exists here."));
                return;
            } else {
                if (LandControlAPI.Server.getRegionAt(player.worldObj, player.getPosition()) == null) {
                    player.addChatComponentMessage(new TextComponentString("No region there."));
                    return;
                }

                server.addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        Gym g = GymAPI.Server.createGym(player.worldObj, player.getPosition());
                        g.updateColoredBlocks();
                    }
                });
                player.addChatComponentMessage(new TextComponentString("Gym created."));
            }
        } else if (args[0].equalsIgnoreCase("addseat")) {
            if (gym == null) {
                player.addChatComponentMessage(new TextComponentString("Gym not found."));
                return;
            }

            if (gym.getSeats().size() >= 10) {
                player.addChatComponentMessage(new TextComponentString("Max seats set."));
                return;
            }

            gym.addSeat(player.getPosition(), player.rotationYaw);
            player.addChatComponentMessage(new TextComponentString("Seat added."));
        } else if (args[0].equalsIgnoreCase("delseat")) {
            if (gym == null) {
                player.addChatComponentMessage(new TextComponentString("Gym not found."));
                return;
            }

            if (!gym.getSeats().containsKey(player.getPosition())) {
                player.addChatComponentMessage(new TextComponentString("Seat not found."));
                return;
            }

            gym.removeSeat(player.getPosition());
            player.addChatComponentMessage(new TextComponentString("Seat removed."));
        } else if (args[0].equalsIgnoreCase("teleportpos")) {
            if (gym == null) {
                player.addChatComponentMessage(new TextComponentString("Gym not found."));
                return;
            }

            gym.setTeleportPos(player.getPosition());
            player.addChatComponentMessage(new TextComponentString("TeleportPos set."));
        } else if (args[0].equalsIgnoreCase("respawn")) {
            if (gym == null) {
                player.addChatComponentMessage(new TextComponentString("Gym not found."));
                return;
            }

            gym.reloadLivingEntities();
            player.addChatComponentMessage(new TextComponentString("Spawned."));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if(!(sender instanceof EntityPlayerMP)) return false;

        return PermissionAPI.hasPermission(((EntityPlayerMP) sender).getUniqueID(), "gym");
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return new ArrayList<>();
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
