package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.ClientProxy;
import com.ipixelmon.gyms.server.ServerProxy;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.gyms.server.CommandGym;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.teams.EnumTeam;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gyms implements IMod {

    private static final List<Gym> gyms = new ArrayList<>();

    @Override
    public String getID() {
        return "gyms";
    }

    @Override
    public void preInit() {
        initGyms();
        EntityRegistry.registerModEntity(EntityGymLeader.class, "entityGymLeader", 487, iPixelmon.instance, 80, 3, false);
        iPixelmon.registerPacket(PacketOpenClaimGui.Handler.class, PacketOpenClaimGui.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketStorePokemon.Handler.class, PacketStorePokemon.class, Side.SERVER);
    }

    @Override
    public void init() {

    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGym());
    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }

    private static void initGyms() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms"));

        try {
            while(result.next()) {
                gyms.add(new Gym(LandControl.getRegion(UUID.fromString("regionID"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Gym getGym(Region region) throws Exception {
        for (Gym gym : gyms) {
            if (gym.getRegion().equals(region)) return gym;
        }

        throw new Exception("Gym not found there.");
    }

    @SideOnly(Side.SERVER)
    public static Gym createGym(World world, BlockPos pos, int power, EnumTeam team, String name) throws Exception {
        Region region = LandControl.getRegion(world, pos);
        if (name == null || name.isEmpty()) {
            throw new Exception("Name is invalid.");
        }

        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", region.id().toString()));

        if (result.next()) {
            throw new Exception("There is already a gym here.");
        }

        result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("name", name));

        if (result.next()) {
            throw new Exception("There is already a gym with that name.");
        }

        InsertForm gymForm = new InsertForm("Gyms");
        gymForm.add("name", name);
        gymForm.add("regionID", region.id().toString());
        gymForm.add("power", power);
        gymForm.add("team", team.name());
        gymForm.add("gymLeaders", "");
        iPixelmon.mysql.insert(Gyms.class, gymForm);
        Gym gym = new Gym(region);
        gyms.add(gym);

        return gym;
    }

    @SideOnly(Side.SERVER)
    public static void deleteGym(Gym gym) throws Exception {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("regionID", gym.getRegion().id().toString()));

        if (!result.next()) {
            throw new Exception("Gym not found.");
        }

        iPixelmon.mysql.delete(Gyms.class, new DeleteForm("Gyms").add("regionID", gym.getRegion().id().toString()));
        gyms.remove(gym);
    }

}
