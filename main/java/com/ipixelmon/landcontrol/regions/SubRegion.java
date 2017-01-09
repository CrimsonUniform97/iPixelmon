package com.ipixelmon.landcontrol.regions;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;

import java.sql.ResultSet;
import java.util.UUID;

/**
 * Created by colby on 1/8/2017.
 */
public class SubRegion extends Region {

    private UUID parentID;

    public SubRegion(UUID parentID, UUID id) {
        super(id);
        this.parentID = parentID;
    }

    @Override
    protected ResultSet getResult() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class,
                new SelectionForm("SubRegions").where("parentID", parentID.toString()).where("id", id.toString()));

        return result;
    }

    @Override
    protected void setViaMySQL(String column, String value) {
        iPixelmon.mysql.update(LandControl.class, new UpdateForm("SubRegions").set(column, value)
                .where("parentID", parentID.toString()).where("id", id.toString()));
    }

}
