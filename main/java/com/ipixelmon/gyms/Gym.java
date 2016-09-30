package com.ipixelmon.gyms;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.teams.EnumTeam;
import com.ipixelmon.mysql.InsertForm;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Gym
{

    private UUID regionID;
    private String name;
    private int power;
    private EnumTeam team;
    private Map<UUID, PixelmonData> pokemon;

    public Gym(UUID regionID) throws Exception
    {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", regionID.toString()));

        if(!result.next())
        {
           throw new Exception("Gym not found.");
        }

        this.regionID = regionID;
        this.name = result.getString("name");
        this.power = result.getInt("power");
        this.team = EnumTeam.valueOf(result.getString("team"));

        // TODO: set from sqldatabase
        this.pokemon = new HashMap<UUID, PixelmonData>();
    }

    public UUID getRegionID()
    {
        return regionID;
    }

    public String getName()
    {
        return name;
    }

    public int getPower()
    {
        return power;
    }

    public EnumTeam getTeam()
    {
        return team;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPower(int power)
    {
        this.power = power;
    }

    public void setTeam(EnumTeam team)
    {
        this.team = team;
    }

    // TODO: update sqldatabase
    public void setPokemon(Map<UUID, PixelmonData> pokemon) {
        this.pokemon = pokemon;
    }

    public Map<UUID, PixelmonData> getPokemon() {
        return pokemon;
    }

    public static Gym createGym(World world, BlockPos pos, int power, EnumTeam team, String name) throws Exception
    {
        Region region = new Region(world, pos);
        if(name == null || name.isEmpty())
        {
            throw new Exception("Name is invalid.");
        }

        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", region.getUUID().toString()));

        if(result.next())
        {
            throw new Exception("There is already a gym here.");
        }

        result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("name", name));

        if(result.next())
        {
            throw new Exception("There is already a gym with that name.");
        }

        InsertForm gymForm = new InsertForm("Gyms");
        gymForm.add("name", name);
        gymForm.add("regionID", region.getUUID().toString());
        gymForm.add("power", power);
        gymForm.add("team", team.name());
        gymForm.add("pokemon", "");
        iPixelmon.mysql.insert(Gyms.class, gymForm);

        return new Gym(region.getUUID());
    }

    public boolean delete()
    {
        iPixelmon.mysql.delete(Gyms.class, new DeleteForm("Gyms").add("regionID", regionID.toString()));

        try
        {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", regionID.toString()));

            if(!result.next())
            {
                throw new Exception("Gym not found.");
            }
        }catch(Exception e)
        {
            return true;
        }

        return false;
    }

}
