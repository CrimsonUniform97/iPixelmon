package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.Tablet;

import java.sql.SQLException;

/**
 * Created by colby on 12/31/2016.
 */
public class ServerProxy extends AppProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        try {
            iPixelmon.mysql.getDatabase().query("CREATE TABLE IF NOT EXISTS tabletItems (" +
                    "player text NOT NULL, " +
                    "price int NOT NULL, " +
                    "item text NOT NULL, " +
                    "postDate datetime NOT NULL DEFAULT NOW());");

            iPixelmon.mysql.getDatabase().query("CREATE TABLE IF NOT EXISTS tabletPixelmon (" +
                    "player text NOT NULL, " +
                    "price int NOT NULL, " +
                    "pixelmonName text NOT NULL, " +
                    "pixelmonData text NOT NULL, " +
                    "postDate datetime NOT NULL DEFAULT NOW());");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "DELETE FROM tabletItems WHERE postDate < DATE_SUB(NOW(), INTERVAL 7 DAY);";
        iPixelmon.mysql.query(query);
        query = "DELETE FROM tabletPixelmon WHERE postDate < DATE_SUB(NOW(), INTERVAL 7 DAY);";
        iPixelmon.mysql.query(query);
    }
}
