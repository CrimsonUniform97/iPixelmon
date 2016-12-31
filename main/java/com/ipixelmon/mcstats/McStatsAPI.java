package com.ipixelmon.mcstats;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mcstats.client.EXPAnimation;
import com.ipixelmon.mcstats.server.EXPValueList;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.util.PlayerUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class McStatsAPI {

    private static long getEXPRequiredToLevelUp(long exp, int level) {
        return getEXPRequiredForLevel(level) - exp;
    }

    private static long getEXPRequiredForLevel(int level) {
        return (1000 * level) + 10 * ((int) Math.pow(level - 1, 2) + (level - 1));
    }

    private static int getLevel(long exp) {
        int level = 0;

        while (true) {
            if (exp >= getEXPRequiredForLevel(level) && exp <= getEXPRequiredForLevel(level + 1)) break;


            level++;
        }

        return level;
    }

    @SideOnly(Side.CLIENT)
    public static class Client {
        public static BlockPos renderPos;
        public static Date renderStartTime;
        public static int renderEXP;
        public static EXPAnimation expAnimation;
        private static Map<GatherType, Long> EXPValues = Maps.newHashMap();

        // TODO: Add more methods for when we render stats on client
        public static long getEXP(GatherType gatherType) {
            return EXPValues.get(gatherType);
        }

        public static void setEXP(GatherType gatherType, long exp) {
            EXPValues.put(gatherType, exp);
        }
    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static EXPValueList expValueList = new EXPValueList();

        public static void giveEXP(UUID playerID, long exp, GatherType gatherType) {
            iPixelmon.mysql.update(McStatsMod.class, new UpdateForm("STATS")
                    .set(gatherType.name(), getEXP(playerID, gatherType) + exp).where("player", playerID.toString()));

            EntityPlayerMP player = PlayerUtil.getPlayer(playerID);

            if (player != null)
                McStatsAPI.Server.updatePlayer(player, gatherType);
        }

        public static void takeEXP(UUID playerID, long exp, GatherType gatherType) {
            long newEXP = getEXP(playerID, gatherType) - exp;
            newEXP = newEXP < 0 ? 0 : newEXP;

            iPixelmon.mysql.update(McStatsMod.class, new UpdateForm("STATS")
                    .set(gatherType.name(), newEXP).where("player", playerID.toString()));
        }

        public static void setEXP(UUID playerID, long exp, GatherType gatherType) {
            iPixelmon.mysql.update(McStatsMod.class, new UpdateForm("STATS")
                    .set(gatherType.name(), exp).where("player", playerID.toString()));
        }

        public static long getEXP(UUID playerID, GatherType gatherType) {
            ResultSet result = iPixelmon.mysql.selectAllFrom(McStatsMod.class,
                    new SelectionForm("STATS").where("player", playerID.toString()));

            try {
                if (result.next()) return result.getLong(gatherType.name());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return 0;
        }

        public static int getLevel(UUID playerID, GatherType gatherType) {
            return McStatsAPI.getLevel(getEXP(playerID, gatherType));
        }

        public static void updatePlayer(EntityPlayerMP player, GatherType gatherType) {
            iPixelmon.network.sendTo(new PacketUpdateEXP(getEXP(player.getUniqueID(), gatherType), gatherType), player);
        }

    }

    public static class Mining {

        private static Map<UUID, Date> coolDownSuperBreaker = Maps.newHashMap();
        private static Map<UUID, ItemStack> superBreakerItems = Maps.newHashMap();

        // TODO: test
        public static void applySuperBreaker(EntityPlayerMP player) throws Exception {
            ItemStack inHand = player.inventory.getCurrentItem();

            if (inHand == null || !(inHand.getItem() instanceof ItemPickaxe)) return;

            if (getSuperBreakerCoolDown(player.getUniqueID()) == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, 10 + (int) getSuperBreakerDuration(player.getUniqueID()));
                coolDownSuperBreaker.put(player.getUniqueID(), calendar.getTime());

                inHand.addEnchantment(Enchantment.efficiency,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, inHand) + 5);

                superBreakerItems.put(player.getUniqueID(), inHand);
            } else {
                throw new Exception("Super Breaker has " + getSuperBreakerCoolDown(player.getUniqueID()) +
                        " seconds to cool down.");
            }

            throw new Exception("Super Breaker enabled for " + getSuperBreakerDuration(player.getUniqueID()) + " seconds!");

        }

        // TODO: Get working
        public static void removeSuperBreakerFromItem(UUID playerID) {
            ItemStack inHand = superBreakerItems.get(playerID);

            if (inHand == null || !(inHand.getItem() instanceof ItemPickaxe)) return;

            if (inHand.isItemEnchanted()) {
                System.out.println("CALLED");
                int currentLvl = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, inHand);
                Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(inHand);
                if(currentLvl == 5) {
                    enchantments.remove(Enchantment.efficiency.effectId);
                } else {
                    enchantments.put(Enchantment.efficiency.effectId, currentLvl - 5);
                }

                EnchantmentHelper.setEnchantments(enchantments, inHand);
            }
        }

        public static ItemStack getSuperBreakerItemForPlayer(UUID playerID) {
            return superBreakerItems.get(playerID);
        }

        public static long getSuperBreakerTimeLeft(UUID playerID) {
            if (!coolDownSuperBreaker.containsKey(playerID)) return 0;

            Calendar base = Calendar.getInstance();
            base.add(Calendar.SECOND, 10);
            long timeLeft = (coolDownSuperBreaker.get(playerID).getTime() - base.getTime().getTime()) / 1000;
            timeLeft = timeLeft <= 0 ? 0 : timeLeft;
            return timeLeft;
        }

        public static long getSuperBreakerDuration(UUID playerID) {
            return (2 + (long) Math.floor(McStatsAPI.Server.getLevel(playerID, GatherType.MINING) / 50));
        }

        public static long getSuperBreakerCoolDown(UUID playerID) {
            if (!coolDownSuperBreaker.containsKey(playerID)) return 0;

            long coolDown = (coolDownSuperBreaker.get(playerID).getTime() - Calendar.getInstance().getTime().getTime()) / 1000;
            coolDown = coolDown <= 0 ? 0 : coolDown;
            return coolDown;
        }

        public static double getDoubleDropChance(UUID playerID) {
            return McStatsAPI.Server.getLevel(playerID, GatherType.MINING) * 0.1;
        }

    }

}
