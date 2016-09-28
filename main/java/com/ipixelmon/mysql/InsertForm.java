package com.ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class InsertForm {

    protected Map<String, String> valueMap;
    protected String tableName;

    public InsertForm(final String parTable) {
        this.valueMap = new HashMap<String, String>();
        this.tableName = parTable;
    }

    public final InsertForm add(final String key, Object value) {
        String strValue = "" + value;
        if(strValue.equalsIgnoreCase("false")) {
            value = "0";
        } else if (strValue.equalsIgnoreCase("true")) {
            value = "1";
        }

        this.valueMap.put(key, "" + value);
        return this;
    }

}
