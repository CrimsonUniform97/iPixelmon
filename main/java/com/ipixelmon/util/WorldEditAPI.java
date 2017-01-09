package com.ipixelmon.util;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.forge.ForgeWorld;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

/**
 * Created by colby on 1/8/2017.
 */
public class WorldEditAPI {

    public static Region getSelection(EntityPlayerMP player, World world) {
        try {
            ForgeWorld forgeWorld = ForgeWorldEdit.inst.getWorld(world);
            LocalSession session = ForgeWorldEdit.inst.getSession(player);

            if (session != null) {
                Region selection = session.getSelection(forgeWorld);
                if (selection != null) {
                    return selection;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
