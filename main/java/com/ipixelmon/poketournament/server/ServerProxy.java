package com.ipixelmon.poketournament.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.poketournament.PokeTournamentMod;
import com.ipixelmon.poketournament.TournamentAPI;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
//        Pixelmon.EVENT_BUS.register( new BattleListener());
        MinecraftForge.EVENT_BUS.register( new PlayerListener());

        CreateForm arenaForm = new CreateForm("Arenas");
        arenaForm.add("region", DataType.TEXT);
        arenaForm.add("name", DataType.TEXT);

        iPixelmon.mysql.createTable(PokeTournamentMod.class, arenaForm);

        TournamentAPI.Server.initArenas();
    }
}
