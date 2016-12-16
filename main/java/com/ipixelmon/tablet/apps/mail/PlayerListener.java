package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.mail.packet.PacketReceiveMail;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colby on 12/14/2016.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // TODO: Look up players send them mail. If they end up being the last on the mail list remove it from the mysql database
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletMail WHERE receiver='" + event.player.getUniqueID().toString() + "';");

        try {
            while(result.next()) {
                iPixelmon.network.sendTo(new PacketReceiveMail(result.getString("sentDate"),
                        UUIDManager.getPlayerName(UUID.fromString(result.getString("sender"))),
                        result.getString("message")), (EntityPlayerMP) event.player);
            }

            iPixelmon.mysql.query("DELETE FROM tabletMail WHERE receiver='" + event.player.getUniqueID().toString() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
