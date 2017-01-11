package com.ipixelmon.gym.server;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class BattleListenerThread implements Runnable {

    private Map<PlayerParticipant, Integer> battleCounter = Maps.newHashMap();

    @Override
    public void run() {
        try {
            BattleRegistry battleRegistry = BattleRegistry.class.newInstance();
            Field f = battleRegistry.getClass().getDeclaredField("battleList");
            f.setAccessible(true);

            while (true) {
                List<BattleControllerBase> battles = (List<BattleControllerBase>) f.get(battleRegistry); //IllegalAccessException

                Map<PixelmonWrapper, Integer> lvlCache = Maps.newHashMap();

                List<PixelmonWrapper> pixelmonWrappers = getPixelmonWrappers(battles.toArray(new BattleControllerBase[battles.size()]));

                for (PixelmonWrapper wrapper : pixelmonWrappers)
                    if (wrapper.getParticipant() instanceof PlayerParticipant)
                        lvlCache.put(wrapper, wrapper.pokemon.getLvl().getLevel());

                long wait = 500L;

                Thread.sleep(wait);

                for (PixelmonWrapper wrapper : pixelmonWrappers)
                    if (lvlCache.containsKey(wrapper))
                        if (lvlCache.get(wrapper) != wrapper.pokemon.getLvl().getLevel())
                            ((PlayerParticipant) wrapper.getParticipant()).wait = false;

                for (BattleControllerBase battle : battles) {
                    for (PlayerParticipant player : battle.getPlayers()) {
                        if (player.wait) {
                            if (!battleCounter.containsKey(player))
                                battleCounter.put(player, 0);

                            battleCounter.put(player, battleCounter.get(player) + 1);

                            if (battleCounter.get(player) > (1000 / wait) * 10)
                                player.wait = false;
                        } else {
                            battleCounter.remove(player);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MinecraftServer.getServer().stopServer();
        }
    }

    private List<PixelmonWrapper> getPixelmonWrappers(BattleControllerBase... battles) {
        List<PixelmonWrapper> pixelmonWrappers = Lists.newArrayList();

        for (BattleControllerBase battle : battles)
            for (PixelmonWrapper wrapper : battle.getActivePokemon())
                if (wrapper != null && wrapper.pokemon != null)
                    if (wrapper.pokemon.playerOwned)
                        pixelmonWrappers.add(wrapper);

        return pixelmonWrappers;
    }
}