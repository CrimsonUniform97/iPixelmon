package com.ipixelmon.uuidmanager;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PlayerListener {

    @SubscribeEvent
    public final void login(final PlayerEvent.PlayerLoggedInEvent event) {
        try {
            final ResultSet result = iPixelmon.mysql.selectAllFrom(UUIDManager.class, new SelectionForm("Players").where("uuid", event.player.getUniqueID().toString()));
            if (result.next())
                iPixelmon.mysql.update(UUIDManager.class, new UpdateForm("Players").set("name", event.player.getGameProfile().getName()).set("nameLower", event.player.getGameProfile().getName().toLowerCase()).where("uuid", event.player.getUniqueID().toString()));
            else
                iPixelmon.mysql.insert(UUIDManager.class, new InsertForm("Players")
                        .add("uuid", event.player.getUniqueID().toString())
                        .add("name", event.player.getGameProfile().getName())
                        .add("nameLower", event.player.getGameProfile().getName().toLowerCase()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
