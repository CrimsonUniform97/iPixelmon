package com.ipixelmon.poketournament.server;

import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.permission.PermissionAPI;
import com.ipixelmon.poketournament.TournamentAPI;
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
import java.util.List;

public class CommandHandler implements ICommand {

    @Override
    public String getCommandName() {
        return "tournament";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tournament create <name>\n/tournament end\n/tournament start\n/tournament delete";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;

        Region region = LandControlAPI.Server.getRegionAt(player.worldObj, player.getPosition());

        if (region == null) {
            player.addChatMessage(new TextComponentString("There is no arena there."));
            return;
        }

        // TODO:
        try {
            switch (args[0].toLowerCase()) {
                case "create": {
                    if(args.length < 2) throw new Exception("Invalid name.");
                    TournamentAPI.Server.createArena(region, args[1]);
                    player.addChatMessage(new TextComponentString("Arena created."));
                    break;
                }
                case "delete": {
                    TournamentAPI.Server.deleteArena(region);
                    player.addChatMessage(new TextComponentString("Arena deleted."));
                    break;
                }
                case "end": {

                    break;
                }
                case "start": {

                    break;
                }
                default: {
                    sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
                    break;
                }
            }
        } catch (Exception e) {
            player.addChatMessage(new TextComponentString(e.getMessage()));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (!(sender instanceof EntityPlayer)) return false;

        return PermissionAPI.hasPermission(((EntityPlayer) sender).getUniqueID(), "tournament");
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
        return -999;
    }
}
