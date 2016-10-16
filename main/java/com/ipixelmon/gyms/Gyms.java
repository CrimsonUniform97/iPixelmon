package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.ClientProxy;
import com.ipixelmon.gyms.client.TileEntityGymInfo;
import com.ipixelmon.gyms.server.BattleListenerThread;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
        EntityRegistry.registerModEntity(EntityGymLeader.class, "entityGymLeader", 487, iPixelmon.instance, 80, 3, false);
        iPixelmon.registerPacket(PacketOpenClaimGui.Handler.class, PacketOpenClaimGui.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketStorePokemon.Handler.class, PacketStorePokemon.class, Side.SERVER);
        GameRegistry.registerTileEntity(TileEntityGymInfo.class, "tileEntityGymInfo");
        GameRegistry.registerBlock(BlockGymInfo.instance);
    }

    @Override
    public void init() {
        initGyms();
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandGym());
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
        for(Gym gym : gyms) {
            gym.spawnGymLeaders();
        }

        new Thread(new BattleListenerThread()).start();
    }

    public static List<Gym> getGyms() {
        return gyms;
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
            while (result.next()) {
                gyms.add(new Gym(UUID.fromString(result.getString("region"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Gym getGym(Region region) throws Exception {
        for (Gym gym : gyms) if (gym.getRegion().equals(region)) return gym;

        throw new Exception("Gym not found there.");
    }

    @SideOnly(Side.SERVER)
    public static Gym createGym(World world, BlockPos pos, int power, EnumTeam team) throws Exception {
        Region region = LandControl.getRegion(world, pos);

        if (iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("region", region.id().toString())).next())
            throw new Exception("There is already a gym here.");

        NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setLong("power", power);
        dataTag.setString("team", team.name());

        InsertForm gymForm = new InsertForm("Gyms");
        gymForm.add("region", region.id().toString());
        gymForm.add("data", dataTag.toString());
        gymForm.add("seats", new NBTTagCompound().toString());
        gymForm.add("gymLeaders", new NBTTagCompound().toString());
        iPixelmon.mysql.insert(Gyms.class, gymForm);
        Gym gym = new Gym(region);
        gyms.add(gym);

        return gym;
    }

    @SideOnly(Side.SERVER)
    public static void deleteGym(Gym gym) throws Exception {
        if (!iPixelmon.mysql.selectAllFrom(Gyms.class, new SelectionForm("Gyms").add("region", gym.getRegion().id().toString())).next())
            throw new Exception("Gym not found.");

        iPixelmon.mysql.delete(Gyms.class, new DeleteForm("Gyms").add("region", gym.getRegion().id().toString()));
        gyms.remove(gym);
    }

}
