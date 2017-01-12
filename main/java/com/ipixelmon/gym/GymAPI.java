package com.ipixelmon.gym;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class GymAPI {

    @SideOnly(Side.CLIENT)
    public static class Client {

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static Set<Gym> gyms = new TreeSet<>();

        public static Gym createGym(World world, BlockPos pos) {
            Region region = LandControlAPI.Server.getRegionAt(world, pos);
            if (region == null) return null;

            if (getGym(pos) != null) return null;

            UUID id = UUID.randomUUID();
            InsertForm insertForm = new InsertForm("Gyms");
            insertForm.add("id", id.toString());
            insertForm.add("region", region.getID().toString());
            insertForm.add("trainers", "[]");
            insertForm.add("seats", "[]");
            insertForm.add("startBattlePlate", "[]");
            insertForm.add("team", "None");
            insertForm.add("prestige", "0");

            iPixelmon.mysql.insert(GymMod.class, insertForm);
            Gym gym = new Gym(id, region.getID());
            gyms.add(gym);
            return gym;
        }

        public static Gym getGym(BlockPos pos) {
            for (Gym gym : gyms) {
                if (gym.getRegion().getBounds().isVecInside(new Vec3(pos.getX(), pos.getY(), pos.getZ()))) {
                    return gym;
                }
            }

            return null;
        }

        public static Gym getGym(UUID id) {
            for(Gym gym : gyms) {
                if(gym.getID().equals(id)) return gym;
            }

            return null;
        }

        public static void initGymSQL() {
            CreateForm createForm = new CreateForm("Gyms");
            createForm.add("id", DataType.TEXT);
            createForm.add("region", DataType.TEXT);
            createForm.add("trainers", DataType.TEXT);
            createForm.add("seats", DataType.TEXT);
            createForm.add("teleportPos", DataType.TEXT);
            createForm.add("team", DataType.TEXT);
            createForm.add("prestige", DataType.INT);
            iPixelmon.mysql.createTable(GymMod.class, createForm);
        }

        public static void loadGyms() {
            ResultSet result = iPixelmon.mysql.selectAllFrom(GymMod.class, new SelectionForm("Gyms"));
            try {
                Gym gym;
                while(result.next()) {
                    gyms.add(gym = new Gym(UUID.fromString(result.getString("id"))));
                    gym.reloadLivingEntities();
                    gym.updateColoredBlocks();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

}
