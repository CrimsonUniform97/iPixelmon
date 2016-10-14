package com.ipixelmon.gyms.server;

import com.ipixelmon.gyms.Gym;
import com.ipixelmon.gyms.Gyms;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.teams.EnumTeam;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandGym implements ICommand {
    @Override
    public String getCommandName() {
        return "gym";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        //TODO: If still getting error check this out, maybe shorten it.
        return "/gym create\n/gym delete\n/gym sync\n/gym addseat\n/gym delseat";
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
                    Gyms.createGym(player.worldObj, playerPos, 0, EnumTeam.None);
                    player.addChatComponentMessage(new ChatComponentText("Gym created."));
                    break;
                }
                case "delete": {
                    Gyms.deleteGym(Gyms.getGym(LandControl.getRegion(player.getEntityWorld(), playerPos)));
                    player.addChatComponentMessage(new ChatComponentText("Gym deleted."));
                    break;
                }
                case "sync": {
                    Gyms.getGym(LandControl.getRegion(player.getEntityWorld(), playerPos)).sync();
                    player.addChatComponentMessage(new ChatComponentText("Gym sync complete."));
                    break;
                }
                case "addseat": {
                    Gym gym = Gyms.getGym(LandControl.getRegion(player.getEntityWorld(), playerPos));

                    if(gym.getSeats().size() >= 10) throw new Exception("Maximum seats reached. (10)");

                    gym.getSeats().add(new BlockPos(player.posX, player.posY, player.posZ));
                    gym.sync();
                    player.addChatComponentMessage(new ChatComponentText("Seat added."));
                    break;
                }
                case "delseat": {
                    Gym gym = Gyms.getGym(LandControl.getRegion(player.getEntityWorld(), playerPos));
                    int size = gym.getSeats().size();
                    gym.getSeats().remove(playerPos);
                    gym.sync();
                    if(size == gym.getSeats().size()) throw new Exception("There is no seat there.");
                    player.addChatComponentMessage(new ChatComponentText("Seat deleted."));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
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
