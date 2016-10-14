package com.ipixelmon.gyms;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by colby on 10/14/2016.
 */
public class GymBattle extends BattleControllerBase {

    public GymBattle(BattleParticipant[] team1, BattleParticipant[] team2) throws Exception {
        super(team1, team2);
    }

    public List<TrainerParticipant> getTrainers() {

        List<TrainerParticipant> trainers = new ArrayList<>();
        Iterator var2 = this.participants.iterator();

        while(var2.hasNext()) {
            BattleParticipant p = (BattleParticipant)var2.next();
            if(p.getType() == ParticipantType.Trainer) trainers.add((TrainerParticipant) p);
        }

        return trainers;
    }

}
