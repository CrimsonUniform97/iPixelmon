package com.ipixelmon.permission;

import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommandPermission implements ICommand {
    @Override
    public String getCommandName() {
        return "permissions";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "permission add <group|player> <perm>\npermission remove <group|player> <perm>\npermission setgroup <player> <group>\npermission list";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            switch (args[0].toLowerCase()) {
                case "add": {
                    Group g = Group.getGroup(args[1]);
                    if (g == null) {

                        UUID player = UUIDManager.getUUID(args[1]);
                        if (player == null) {
                            throw new Exception("Group not found.");
                        }

                        new Player(player).addPermission(args[2]);
                    } else {
                        g.addPermission(args[2]);
                    }

                    throw new Exception("Permission added.");
                }
                case "remove": {
                    Group g = Group.getGroup(args[1]);
                    if (g == null) {

                        UUID player = UUIDManager.getUUID(args[1]);
                        if (player == null) {
                            throw new Exception("Group not found.");
                        }

                        new Player(player).removePermission(args[2]);
                    } else {
                        g.removePermission(args[2]);
                    }

                    throw new Exception("Permission removed.");
                }
                case "setgroup": {
                    UUID player = UUIDManager.getUUID(args[1]);
                    if (player == null) throw new Exception("Player not found.");
                    Group group = Group.getGroup(args[2]);
                    if (group == null) throw new Exception("Group not found.");

                    new Player(player).setGroup(group);
                    throw new Exception("Player group set.");
                }
                case "list": {

                    break;
                }
                default: {
                    sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
                    break;
                }
            }
        } catch (Exception e) {
            sender.addChatMessage(new TextComponentString(e.getMessage()));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (!(sender instanceof EntityPlayer)) return true;
        EntityPlayerMP player = (EntityPlayerMP) sender;
        return new Player(player.getUniqueID()).hasPermission("permissions.edit");
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        ArrayList list = new ArrayList();
        if (args[0].equalsIgnoreCase("setgroup")) {
            if (args.length == 2) {
                list.addAll(Arrays.asList(server.getPlayerList().getAllUsernames()));
            } else if (args.length == 3) {
                for (Group g : Group.getGroups()) list.add(g.getName());
            }
        } else {
            if (args.length == 2) {
                for (Group g : Group.getGroups()) list.add(g.getName());
            }
        }
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return -999;
    }
}
