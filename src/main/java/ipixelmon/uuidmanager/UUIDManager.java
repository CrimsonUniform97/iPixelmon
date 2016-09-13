package ipixelmon.uuidmanager;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.SelectionForm;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class UUIDManager implements IMod {

    @Override
    public final String getID() {
        return "uuidmanager";
    }

    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {

    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {

    }

    @Override
    public final Class<? extends CommonProxy> clientProxyClass() {
        return null;
    }

    @Override
    public final Class<? extends CommonProxy> serverProxyClass() {
        return ipixelmon.uuidmanager.ServerProxy.class;
    }

    @Override
    public final IGuiHandler getGuiHandler() {
        return null;
    }

    public static final String getPlayerName(final UUID uuid) {
        try {
            ResultSet result = iPixelmon.mysql.selectAllFrom(UUIDManager.class, new SelectionForm("Players").add("uuid", uuid.toString()));
            if (result.next()) return result.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final UUID getUUID(final String name) {
        try {
            ResultSet result = iPixelmon.mysql.selectAllFrom(UUIDManager.class, new SelectionForm("Players").add("nameLower", name.toLowerCase()));
            if (result.next()) return UUID.fromString(result.getString("uuid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Map<UUID, String> getNames(final List<UUID> uuids)
    {
        Map<UUID, String> names = new HashMap<>();

        if(uuids.isEmpty())
        {
            return names;
        }

        try
        {
            StringBuilder builder = new StringBuilder();
            for(UUID uuid : uuids)
            {
                builder.append(uuid.toString());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);

            ResultSet result = iPixelmon.mysql.query("SELECT * FROM uuidmanagerPlayers WHERE uuid IN ('" + builder.toString() + "');");
            while(result.next())
            {
                names.put(UUID.fromString(result.getString("uuid")), result.getString("name"));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return names;
    }

    public static final Map<String, UUID> getUUIDs(final List<String> names)
    {
        Map<String, UUID> uuids = new HashMap<>();

        if(names.isEmpty())
        {
            return uuids;
        }

        try
        {
            StringBuilder builder = new StringBuilder();
            for(String name : names)
            {
                builder.append(name.toLowerCase());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);

            ResultSet result = iPixelmon.mysql.query("SELECT * FROM uuidmanagerPlayers WHERE nameLower IN ('" + builder.toString() + "');");
            while(result.next())
            {
                uuids.put(result.getString("name"), UUID.fromString(result.getString("uuid")));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return uuids;
    }
}
