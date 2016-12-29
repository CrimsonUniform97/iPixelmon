package com.ipixelmon.mcstats;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class McStatsAPI {

    public static long getEXPRequiredToLevelUp(int exp, int level) {
        return getEXPRequiredForLevel(level) - exp;
    }

    public static long getEXPRequiredForLevel(int level) {
        return (1000 * level) + 10 * ((int) Math.pow(level - 1, 2) + (level - 1));
    }

    public static int getLevel(int exp) {
        int level = 0;

        while(true) {
            if(exp >= getEXPRequiredForLevel(level) && exp <= getEXPRequiredForLevel(level + 1)) break;


            level++;
        }

        return level;
    }

    @SideOnly(Side.CLIENT)
    public static class Client {
        public static long EXP = 0;
        public static BlockPos renderPos;
        public static Date renderStartTime;
        public static int renderEXP;
    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static void giveEXP(UUID playerID, long exp) {
            iPixelmon.mysql.update(McStatsMod.class, new UpdateForm("STATS")
                    .set("exp", getEXP(playerID) + exp).where("player", playerID.toString()));
        }

        public static void takeEXP(UUID playerID, long exp) {
            long newEXP =  getEXP(playerID) - exp;
            newEXP = newEXP < 0 ? 0 : newEXP;

            iPixelmon.mysql.update(McStatsMod.class, new UpdateForm("STATS")
                    .set("exp", newEXP).where("player", playerID.toString()));
        }

        public static void setEXP(UUID playerID, long exp) {
            iPixelmon.mysql.update(McStatsMod.class, new UpdateForm("STATS")
                    .set("exp", exp).where("player", playerID.toString()));
        }

        public static long getEXP(UUID playerID) {
            ResultSet result = iPixelmon.mysql.selectAllFrom(McStatsMod.class,
                    new SelectionForm("STATS").where("player", playerID.toString()));

            try {
                if(result.next()) return result.getLong("exp");
            }catch(SQLException e) {
                e.printStackTrace();
            }

            return 0;
        }

        public static void updatePlayer(EntityPlayerMP player) {
            iPixelmon.network.sendTo(new PacketUpdateEXP(getEXP(player.getUniqueID())), player);
        }

    }

}
