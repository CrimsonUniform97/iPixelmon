package com.ipixelmon.landcontrol;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.landcontrol.client.ClientProxy;
import com.ipixelmon.landcontrol.server.ServerProxy;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LandControl implements IMod {

    protected static final List<Region> regions = new ArrayList<Region>();

    @Override
    public String getID() {
        return "landcontrol";
    }

    @Override
    public void preInit() {
        initRegions();
    }

    @Override
    public void init() {
        iPixelmon.registerPacket(PacketOpenRegionInfo.Handler.class, PacketOpenRegionInfo.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketEditMemberRequest.Handler.class, PacketEditMemberRequest.class, Side.SERVER);
        iPixelmon.registerPacket(PacketEditMemberResponse.Handler.class, PacketEditMemberResponse.class, Side.CLIENT);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

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

    private static void initRegions() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class, new SelectionForm("Regions"));

        try {
            while(result.next()) {
                regions.add(new Region(UUID.fromString(result.getString("uuid"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.SERVER)
    public static Region createRegion(UUID owner, World world, int xMin, int xMax, int zMin, int zMax) throws Exception {

        for (Region region : regions) {
            if (region.getMin().getX() <= xMax && xMin <= region.getMax().getX()
                    && region.getMin().getZ() <= zMax && zMin <= region.getMax().getZ()) {
                throw new Exception("Overlapping another region.");
            }
        }

        InsertForm insertForm = new InsertForm("Regions");
        UUID id = UUID.randomUUID();
        insertForm.add("uuid", id);
        insertForm.add("owner", owner.toString());
        insertForm.add("members", "");
        insertForm.add("world", world.getWorldInfo().getWorldName());
        insertForm.add("xMin", xMin);
        insertForm.add("xMax", xMax);
        insertForm.add("zMin", zMin);
        insertForm.add("zMax", zMax);

        iPixelmon.mysql.insert(LandControl.class, insertForm);

        Region region = new Region(id);
        regions.add(region);
        return new Region(id);
    }

    @SideOnly(Side.SERVER)
    public static Region getRegion(World world, BlockPos pos) throws Exception {
        for(Region region : regions) {
            if(region.getWorldClient().equalsIgnoreCase(world.getWorldInfo().getWorldName())
                    && region.contains(pos)) {
                return region;
            }
        }

        throw new Exception("There is no region there.");
    }

    public static Region getRegion(UUID id) throws Exception {
        for(Region region : regions) {
            if(region.id().equals(id)) return region;
        }

        throw new Exception("There is no region there.");
    }

    public static Region getRegion(String worldName, BlockPos pos) throws Exception {
        for(Region region : regions) {
            if(region.getWorldClient().equalsIgnoreCase(worldName) && region.contains(pos)) {
                return region;
            }
        }

        throw new Exception("There is no region there.");
    }

}
