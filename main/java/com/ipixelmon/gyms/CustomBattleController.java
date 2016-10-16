package com.ipixelmon.gyms;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.BattleStage;
import com.pixelmonmod.pixelmon.battles.controller.GlobalStatusController;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.enums.EnumBattleType;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by colby on 10/15/2016.
 */
public class CustomBattleController extends BattleControllerBase {

    public CustomBattleController(BattleParticipant[] team1, BattleParticipant[] team2, EnumBattleType battleType) throws Exception {
        super(team1, team2, battleType);
        BattleParticipant[] var5 = team1;
        int var6 = team1.length;

        int var7;
        BattleParticipant p;
        for (var7 = 0; var7 < var6; ++var7) {
            p = var5[var7];
            p.team = 0;
            this.participants.add(p);
        }

        var5 = team2;
        var6 = team2.length;

        for (var7 = 0; var7 < var6; ++var7) {
            p = var5[var7];
            p.team = 1;
            this.participants.add(p);
        }

        BattleRegistry.registerBattle(this);
        this.battleLog = new BattleLog(this);
        this.battleType = battleType;
    }

}
