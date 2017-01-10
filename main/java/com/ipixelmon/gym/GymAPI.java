package com.ipixelmon.gym;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.mysql.InsertForm;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

/**
 * Created by colby on 1/10/2017.
 */
public class GymAPI {

    @SideOnly(Side.CLIENT)
    public static class Client {

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static List<Gym> gyms = Lists.newArrayList();

        public static Gym createGym(BlockPos pos) {
            Region region = LandControlAPI.Server.getRegionAt(pos);
            if (region == null) return null;

            if (getGym(pos) != null) return null;

            UUID id = UUID.randomUUID();
            InsertForm insertForm = new InsertForm("Gyms");
            insertForm.add("id", id.toString());
            insertForm.add("region", region.getID().toString());
            insertForm.add("trainers", "[]");
            insertForm.add("seats", "[]");
            insertForm.add("team", "None");
            insertForm.add("prestige", "0");

            iPixelmon.mysql.insert(GymMod.class, insertForm);
            Gym gym = new Gym(id);
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

        public static void initGymSQL() {
            CreateForm createForm = new CreateForm("Gyms");
            createForm.add("id", DataType.TEXT);
            createForm.add("region", DataType.TEXT);
            createForm.add("trainers", DataType.TEXT);
            createForm.add("seats", DataType.TEXT);
            createForm.add("team", DataType.TEXT);
            createForm.add("prestige", DataType.INT);
            iPixelmon.mysql.createTable(GymMod.class, createForm);
        }

    }

}
