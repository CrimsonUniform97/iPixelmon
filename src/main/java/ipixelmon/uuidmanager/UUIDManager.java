package ipixelmon.uuidmanager;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.SelectionForm;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final UUID getUUID(final String name) {
        try {
            ResultSet result = iPixelmon.mysql.selectAllFrom(UUIDManager.class, new SelectionForm("Players").add("nameLower", name.toLowerCase()));
            if (result.next()) return UUID.fromString(result.getString("uuid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
