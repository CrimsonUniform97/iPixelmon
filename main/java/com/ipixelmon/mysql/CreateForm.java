package com.ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class CreateForm {

    protected Map<String, DataType> dataTypeMap;
    protected String table;

    public CreateForm(final String parTable) {
        this.dataTypeMap = new HashMap<String, DataType>();
        this.table = parTable;
    }

    public final CreateForm add(final String key,final  DataType type) {
        this.dataTypeMap.put(key, type);
        return this;
    }

    public final CreateForm setTable(String parTable) { this.table = parTable; return this; }

}
