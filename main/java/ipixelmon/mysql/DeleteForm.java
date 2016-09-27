package ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class DeleteForm {

    protected Map<String, String> deleteForms;
    protected String tableName;
    protected int limit = -1;

    public DeleteForm(final String tableName) {
        this.tableName = tableName;
        deleteForms = new HashMap<String, String>();
    }

    public final DeleteForm add(final String key, Object value) {
        String strValue = "" + value;
        if(strValue.equalsIgnoreCase("false")) {
            value = "0";
        } else if (strValue.equalsIgnoreCase("true")) {
            value = "1";
        }


        this.deleteForms.put(key, "" + value);
        return this;
    }

    public final DeleteForm setLimit(final int limit) {
        this.limit = limit;
        return this;
    }

}
