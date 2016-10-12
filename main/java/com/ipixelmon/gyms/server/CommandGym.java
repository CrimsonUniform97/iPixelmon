package com.ipixelmon.gyms.server;

import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.teams.EnumTeam;
import com.mojang.authlib.GameProfile;
import com.ipixelmon.gyms.Gym;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class CommandGym implements ICommand {
    @Override
    public String getCommandName() {
        return "gym";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/gym create <name>\n/gym delete\n/gym update\n/gym addslot\n/gym delslot";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<String>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (!MinecraftServer.getServer().getConfigurationManager().canSendCommands(new GameProfile(player.getUniqueID(), player.getName()))) {
            player.addChatComponentMessage(new ChatComponentText("You do not have permission for that command."));
            return;
        }

        if (args.length == 0) {
            player.addChatComponentMessage(new ChatComponentText(getCommandUsage(sender)));
            return;
        }

        String cmd = args[0].toLowerCase();

        BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);

        try {
            switch (cmd.toLowerCase()) {
                case "create": {
                    if (args.length != 2)
                        throw new Exception(getCommandUsage(sender));
                    Gym.createGym(player.worldObj, playerPos, 0, EnumTeam.None, args[1]);
                    player.addChatComponentMessage(new ChatComponentText("Gym created."));
                    break;
                }
                case "delete": {
                    Gym.instance.getGym(Region.instance.getRegion(player.getEntityWorld(), playerPos)).delete();
                    player.addChatComponentMessage(new ChatComponentText("Gym deleted."));
                    break;
                }
                case "update": {
                    Gym.instance.getGym(Region.instance.getRegion(player.getEntityWorld(), playerPos)).update();
                    player.addChatComponentMessage(new ChatComponentText("Gym updated."));
                    break;
                }
                case "addslot": {
                    Gym gym = Gym.instance.getGym(Region.instance.getRegion(player.getEntityWorld(), playerPos));
                    List<BlockPos> slots = gym.getDisplayBlocks();
                    slots.add(new BlockPos(player.posX, player.posY, player.posZ));
                    gym.setDisplayBlocks(slots);
                    break;
                }
                case "delslot": {
                    Gym gym = Gym.instance.getGym(Region.instance.getRegion(player.getEntityWorld(), playerPos));
                    List<BlockPos> slots = gym.getDisplayBlocks();
                    slots.remove(new BlockPos(player.posX, player.posY, player.posZ));
                    gym.setDisplayBlocks(slots);
                    break;
                }
            }
        } catch (Exception e) {
            player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayer;
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
