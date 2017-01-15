package com.ipixelmon.landcontrol;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.regions.EnumRegionProperty;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.landcontrol.regions.SubRegion;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.util.ArrayUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by colby on 1/7/2017.
 */
public class LandControlAPI {

    @SideOnly(Side.CLIENT)
    public static class Client {

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static final EventBus EVENT_BUS = new EventBus();
        public static Set<Region> regions = new TreeSet<>();

        public static ToolCupboardTileEntity getTileEntity(World world, BlockPos pos) {
            Map<BlockPos, TileEntity> tileEntityMap = world.getChunkFromBlockCoords(pos).getTileEntityMap();
            for (BlockPos p : tileEntityMap.keySet()) {
                TileEntity tileEntity = tileEntityMap.get(p);
                if (tileEntity instanceof ToolCupboardTileEntity)
                    return ((ToolCupboardTileEntity) tileEntity).getBaseTile();
            }

            return null;
        }

        public static Region getRegion(UUID id) {
            for(Region region : regions) {
                if(region.getID().equals(id)) {
                    return region;
                }

                for(SubRegion subRegion : region.getSubRegions()) {
                    if(subRegion.getID().equals(id)) return subRegion;
                }
            }

            return null;
        }

        public static Region getRegionAt(World world, BlockPos pos) {
            for (Region region : LandControlAPI.Server.regions) {
                if(region.getWorld().equals(world)) {
                    Vec3 vec3 = new Vec3(pos.getX(), pos.getY(), pos.getZ());
                    if (region.getBounds().isVecInside(vec3)) {
                        for(SubRegion subRegion : region.getSubRegions()) {
                            if(subRegion.getBounds().isVecInside(vec3)) return subRegion;
                        }
                        return region;
                    }
                }
            }
            return null;
        }


        public static Region createRegion(World world, BlockPos min, BlockPos max) {
            BlockPos minPos = new BlockPos(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()), Math.min(min.getZ(), max.getZ()));
            BlockPos maxPos = new BlockPos(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()), Math.max(min.getZ(), max.getZ()));
            UUID id = UUID.randomUUID();

            InsertForm insertForm = new InsertForm("Regions");
            insertForm.add("id", id.toString());
            insertForm.add("owner", id.toString());
            insertForm.add("members", ArrayUtil.toString(new String[]{""}));
            insertForm.add("world", world.getWorldInfo().getWorldName());

            insertForm.add("min", ArrayUtil.toString(new String[]{
                    String.valueOf(minPos.getX()),
                    String.valueOf(minPos.getY()),
                    String.valueOf(minPos.getZ())
            }));

            insertForm.add("max", ArrayUtil.toString(new String[]{
                    String.valueOf(maxPos.getX()),
                    String.valueOf(maxPos.getY()),
                    String.valueOf(maxPos.getZ())
            }));


            AxisAlignedBB bounds = AxisAlignedBB.fromBounds(minPos.getX(), minPos.getY(), minPos.getZ(),
                    maxPos.getX(), maxPos.getY(), maxPos.getZ());

            for (Region region : regions) if (region.getBounds().intersectsWith(bounds)) return null;

            iPixelmon.mysql.insert(LandControl.class, insertForm);

            Region region;
            regions.add(region = new Region(id));
            region.init();

            return region;
        }

        public static SubRegion createSubRegion(BlockPos min, BlockPos max) {
            BlockPos minPos = new BlockPos(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()), Math.min(min.getZ(), max.getZ()));
            BlockPos maxPos = new BlockPos(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()), Math.max(min.getZ(), max.getZ()));

            AxisAlignedBB bounds = AxisAlignedBB.fromBounds(minPos.getX(), minPos.getY(), minPos.getZ(),
                    maxPos.getX(), maxPos.getY(), maxPos.getZ());

            Region parentRegion = null;

            for (Region region : regions) {
                if (region.getBounds().intersectsWith(bounds)) {
                    parentRegion = region;
                    break;
                }
            }

            System.out.println("BOOM");

            /**
             * Did not find a parent region
             */
            if (parentRegion == null) return null;

            System.out.println("Parent Region found.");

            System.out.println(parentRegion.getSubRegions().size());

            for (SubRegion subRegion : parentRegion.getSubRegions()) {
                /**
                 * Overlaps another SubRegion
                 */
                if (subRegion.getBounds().intersectsWith(bounds)) {
                    System.out.println("Intersects");
                    return null;
                }
            }

            System.out.println("Doesn't intersect");

            UUID id = UUID.randomUUID();

            InsertForm insertForm = new InsertForm("SubRegions");
            insertForm.add("id", id.toString());
            insertForm.add("owner", parentRegion.getOwner().toString());
            insertForm.add("parentID", parentRegion.getID().toString());
            insertForm.add("members", ArrayUtil.toString(new String[]{}));
            insertForm.add("world", parentRegion.getWorld().getWorldInfo().getWorldName());

            insertForm.add("min", ArrayUtil.toString(new String[]{
                    String.valueOf(minPos.getX()),
                    String.valueOf(minPos.getY()),
                    String.valueOf(minPos.getZ())
            }));

            insertForm.add("max", ArrayUtil.toString(new String[]{
                    String.valueOf(maxPos.getX()),
                    String.valueOf(maxPos.getY()),
                    String.valueOf(maxPos.getZ())
            }));

            iPixelmon.mysql.insert(LandControl.class, insertForm);

            SubRegion region = new SubRegion(parentRegion.getID(), id);
            region.init();
            parentRegion.getSubRegions().add(region);

            return region;
        }

        public static void initRegions() {

            try {
                String columns = "(" +
                        "id text NOT NULL, " +
                        "min text NOT NULL, " +
                        "max text NOT NULL, " +
                        "owner text NOT NULL, " +
                        "members text NOT NULL, " +
                        "enterMsg text, " +
                        "leaveMsg text, " +
                        "world text,";

                StringBuilder builder = new StringBuilder(columns);

                for(EnumRegionProperty property : EnumRegionProperty.values())
                    builder.append(" " + property.name() + " boolean NOT NULL DEFAULT 1,");

                builder.deleteCharAt(builder.length() - 1);

                builder.append(");");


                iPixelmon.mysql.getDatabase().query("CREATE TABLE IF NOT EXISTS landControlRegions " + builder.toString());

                builder = new StringBuilder(columns);
                builder.insert(1, "parentID text NOT NULL, ");

                for(EnumRegionProperty property : EnumRegionProperty.values())
                    builder.append(" " + property.name() + " boolean NOT NULL DEFAULT 1,");

                builder.deleteCharAt(builder.length() - 1);

                builder.append(");");

                iPixelmon.mysql.getDatabase().query("CREATE TABLE IF NOT EXISTS landControlSubRegions " + builder.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class, new SelectionForm("Regions"));
            try {
                Region region;
                while (result.next()) {
                    regions.add(region = new Region(UUID.fromString(result.getString("id"))));
                    region.init();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
