package com.ipixelmon.landcontrol.regions;

import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.util.WorldEditAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

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
        return "/region create|delete";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = (EntityPlayerMP) sender;

        if(args[0].equalsIgnoreCase("create")) {
            com.sk89q.worldedit.regions.Region region = WorldEditAPI.getSelection(player, player.getEntityWorld());

            if (region != null) {
                BlockPos center = new BlockPos(region.getCenter().getBlockX(), region.getCenter().getBlockY(),
                        region.getCenter().getBlockZ());

                BlockPos min = new BlockPos(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
                BlockPos max = new BlockPos(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());

                if (LandControlAPI.Server.getRegionAt(player.worldObj, center) != null) {
                    /**
                     * Create SubRegion
                     */
                    SubRegion s;
                    try {
                        s = LandControlAPI.Server.createSubRegion(min, max);
                    } catch (Exception e) {
                        player.addChatComponentMessage(new TextComponentString(e.getMessage()));
                        return;
                    }

                    s.setOwner(player.getUniqueID());
                    player.addChatComponentMessage(new TextComponentString("SubRegion created."));
                } else {
                    /**
                     * Create Region
                     */
                    Region r;
                    try {
                        r = LandControlAPI.Server.createRegion(player.worldObj, min, max);
                    } catch (Exception e) {
                        player.addChatComponentMessage(new TextComponentString(e.getMessage()));
                        return;
                    }

                    r.setOwner(player.getUniqueID());
                    player.addChatComponentMessage(new TextComponentString("Region created."));
                }
            }
        } else if(args[0].equalsIgnoreCase("delete")) {
            Region region = LandControlAPI.Server.getRegionAt(player.getEntityWorld(), player.getPosition());
            region.delete();

            if(region instanceof SubRegion) {
                player.addChatComponentMessage(new TextComponentString("SubRegion deleted."));
            } else {
                player.addChatComponentMessage(new TextComponentString("Region deleted."));
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

}
