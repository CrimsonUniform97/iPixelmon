package ipixelmon.uuidmanager;

import ipixelmon.CommonProxy;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.CreateForm;
import ipixelmon.mysql.DataType;
import ipixelmon.mysql.UpdateForm;
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
        FMLCommonHandler.instance().bus().register(new PlayerListener());

        final CreateForm playersForm = new CreateForm("Players");
        playersForm.add("uuid", DataType.TEXT);
        playersForm.add("name", DataType.TEXT);
        playersForm.add("nameLower", DataType.TEXT);

        iPixelmon.db.createTable(UUIDManager.class, playersForm);

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
                    final ResultSet result = iPixelmon.db.query("SELECT * FROM uuidmanagerPlayers LIMIT " + lastRun + "," + (lastRun + limit) + ";");
                    lastRun += limit;
                    final List<UUID> uuids = new ArrayList<>();
                    int i = 0;
                    while (result.next()) {
                        if (i < limit) {
                            uuids.add(UUID.fromString(result.getString("uuid")));
                            i++;
                        } else {
                            break;
                        }
                    }

                    final NameFetcher fetcher = new NameFetcher(uuids);
                    final Map<UUID, String> map = fetcher.call();

                    map.forEach((uuid, name) -> {
                        if(uuid != null && name != null && !uuid.toString().isEmpty() && !name.isEmpty()) iPixelmon.db.update(UUIDManager.class, new UpdateForm("Players").set("name", name).set("nameLower", name.toLowerCase()).where("uuid", uuid.toString()));
                    });

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
