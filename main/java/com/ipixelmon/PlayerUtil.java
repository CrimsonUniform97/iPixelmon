package com.ipixelmon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Created by colby on 11/5/2016.
 */
public class PlayerUtil {

    @SideOnly(Side.SERVER)
    public static final boolean isPlayerOnline(UUID playerUUID) {
        return getPlayer(playerUUID) != null;
    }

    @SideOnly(Side.SERVER)
    public static final EntityPlayer getPlayer(UUID playerUUID) {
        for (WorldServer worldServer : MinecraftServer.getServer().worldServers)
            for (EntityPlayer player : worldServer.playerEntities)
                if(player.getUniqueID().equals(playerUUID)) return player;

        return null;
    }

}
