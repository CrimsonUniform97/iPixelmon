package com.ipixelmon.gyms;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.teams.EnumTeam;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 10/13/2016.
 */
public class Gym2 {

    private static final int[] levels = {0, 2000, 4000, 8000, 12000, 16000, 20000, 30000, 40000, 50000};

    private UUID region;
    private List<EntityGymLeader> gymLeaders;
    private Map<EntityPlayerMP, BattleControllerBase> battles;
    private List<BlockPos> seats;
    private NBTTagCompound tagData, tagSeats, tagGymLeaders;

    protected Gym2(UUID region) throws Exception {
        this.region = region;

        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("region", region.toString()));

        if (result.next()) {
            tagData = JsonToNBT.getTagFromJson(result.getString("data"));
            tagSeats = JsonToNBT.getTagFromJson(result.getString("seats"));
            tagGymLeaders = JsonToNBT.getTagFromJson(result.getString("gymLeaders"));
        }
    }

    public long getPower() {
        return tagData.getLong("power");
    }

    public EnumTeam getTeam() {
        return EnumTeam.valueOf(tagData.getString("team"));
    }

    public Region getRegion() throws Exception {
        return LandControl.getRegion(region);
    }

    public List<BlockPos> getSeats() {
        if (seats == null) {
            String[] data = tagSeats.getString("seats").contains(";") ? tagSeats.getString("seats").split(";") : new String[]{tagSeats.getString("seats")};
            seats = new ArrayList<>();

            String[] data2;
            for (String s : data) {
                if(!s.isEmpty()) {
                    data2 = s.split(",");
                    seats.add(new BlockPos(Integer.parseInt(data2[0]), Integer.parseInt(data2[1]), Integer.parseInt(data2[2])));
                }
            }
        }

        return seats;
    }

    @SideOnly(Side.SERVER)
    public List<EntityGymLeader> getGymLeaders() throws Exception {
        if (gymLeaders == null) {
            String[] data = tagGymLeaders.getString("gymLeaders").contains(";") ? tagGymLeaders.getString("gymLeaders").split(";") : new String[]{tagGymLeaders.getString("gymLeaders")};
            gymLeaders = new ArrayList<>();

            UUID player;
            EntityPixelmon pixelmon;

            String[] data2;
            for (String s : data) {
                if(!s.isEmpty()) {
                    data2 = s.split(",");

                    player = UUID.fromString(data2[0]);
                    pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(data2[1], MinecraftServer.getServer().getEntityWorld());
                    pixelmon.setHealth(pixelmon.getMaxHealth());
                    pixelmon.setIsShiny(Boolean.parseBoolean(data2[2]));
                    pixelmon.setForm(Integer.parseInt(data2[3]));
                    pixelmon.getLvl().setLevel(Integer.parseInt(data2[4]));
                    pixelmon.caughtBall = EnumPokeballs.PokeBall;
                    pixelmon.friendship.initFromCapture();
                    gymLeaders.add(new EntityGymLeader(getRegion().getWorldServer(), getSeats().get(Integer.parseInt(data2[5])), pixelmon, player));
                }
            }
        }

        return gymLeaders;
    }

}
