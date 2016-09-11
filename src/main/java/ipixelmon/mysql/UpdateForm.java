package ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class UpdateForm {

    protected Map<String, String> updateFormsSet, updateFormsWhere;
    protected String tableName;

    public UpdateForm(final String tableName) {
        this.tableName = tableName;
        updateFormsSet = new HashMap<String, String>();
        updateFormsWhere = new HashMap<String, String>();
    }

    public final UpdateForm set(final String key, Object value) {
        String strValue = "" + value;
        if(strValue.equalsIgnoreCase("false")) {
            value = "0";
        } else if (strValue.equalsIgnoreCase("true")) {
            value = "1";
        }

        this.updateFormsSet.put(key, "" +  value);
        return this;
    }

    public final UpdateForm where(final String key, Object value) {
        String strValue = "" + value;
        if(strValue.equalsIgnoreCase("false")) {
            value = "0";
        } else if (strValue.equalsIgnoreCase("true")) {
            value = "1";
        }

        this.updateFormsWhere.put(key, "" + value);
        return this;
    }

}
