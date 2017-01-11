package com.ipixelmon.gym;

import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.enums.EnumBattleType;

public class BattleController extends BattleControllerBase {

    public BattleController(BattleParticipant[] team1, BattleParticipant[] team2, EnumBattleType battleType) throws Exception {
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
