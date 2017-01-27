package com.ipixelmon.uuidmanager;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.mysql.UpdateForm;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ServerProxy extends CommonProxy {

    @Override
    public final void preInit() {}

    @Override
    public final void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());

        final CreateForm playersForm = new CreateForm("Players");
        playersForm.add("uuid", DataType.TEXT);
        playersForm.add("name", DataType.TEXT);
        playersForm.add("nameLower", DataType.TEXT);

        iPixelmon.mysql.createTable(UUIDManager.class, playersForm);

        new Thread(new CacheThread()).start();
    }

    public final class CacheThread implements Runnable {

        private int lastRun = 0;
        private int limit = 600;
        private long durationMinutes = 10L;

        @Override
        public final void run() {
            while (true) {
                try {
                    final ResultSet result = iPixelmon.mysql.query("SELECT * FROM uuidmanagerPlayers LIMIT " + lastRun + "," + (lastRun + limit) + ";");
                    lastRun += limit;
                    final List<UUID> uuids = new ArrayList<UUID>();
                    int i = 0;
                    while (result.next()) {
                        if (i < limit) {
                            uuids.add(UUID.fromString(result.getString("uuid")));
                            i++;
                        } else {
                            break;
                        }
                    }

                    // TODO: Causes error if server restarts quickly. Need to fix this by possibly using another API or something.
                    final NameFetcher fetcher = new NameFetcher(uuids);
                    final Map<UUID, String> map = fetcher.call();

                    for(UUID uuid : map.keySet())
                    {
                        if(uuid != null && map.get(uuid) != null && !uuid.toString().isEmpty() && !map.get(uuid).isEmpty()) iPixelmon.mysql.update(UUIDManager.class, new UpdateForm("Players").set("name", map.get(uuid)).set("nameLower", map.get(uuid).toLowerCase()).where("uuid", uuid.toString()));
                    }

                    if (i < limit) {
                        lastRun = 0;
                    }

                    Thread.sleep(60000L * durationMinutes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
