package com.ipixelmon.landcontrol.server;

import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.server.regions.Region;
import com.ipixelmon.util.WorldEditAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;

/**
 * Created by colby on 1/8/2017.
 */
public class CommandRegion extends CommandBase {

    @Override
    public String getCommandName() {
        return "region";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/region create|delete\n/region toggle pvp|damage|build|mobs|pixelmon" +
                "\n/region setWelcome <message>\n/region setLeave <message>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(!(sender instanceof EntityPlayer)) return;

        EntityPlayerMP player = (EntityPlayerMP) sender;

        com.sk89q.worldedit.regions.Region region = WorldEditAPI.getSelection(player, player.getEntityWorld());

        if(region != null) {
            BlockPos min = new BlockPos(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
            BlockPos max = new BlockPos(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());
            Region r = LandControlAPI.Server.createRegion(min, max);
            r.setOwner(player.getUniqueID());
        }
    }
}
