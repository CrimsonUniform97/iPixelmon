package com.ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class SelectionForm {
    protected Map<String, String> selectionForms;
    protected String tableName;
    protected int limit = -1;

    public SelectionForm(final String tableName) {
        this.tableName = tableName;
        selectionForms = new HashMap<String, String>();
    }

    public final SelectionForm add(final String key, Object value) {
        String strValue = "" + value;
        if(strValue.equalsIgnoreCase("false")) {
            value = "0";
        } else if (strValue.equalsIgnoreCase("true")) {
            value = "1";
        }

        this.selectionForms.put(key, ""+ value);
        return this;
    }

    public final SelectionForm setLimit(final int limit) {
        this.limit = limit;
        return this;
    }
}
