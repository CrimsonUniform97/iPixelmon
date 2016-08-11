package ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class InsertForm {

    protected Map<String, String> valueMap;
    protected String tableName;

    public InsertForm(final String parTable) {
        this.valueMap = new HashMap<String, String>();
        this.tableName = parTable;
    }

    public final InsertForm add(final String key, final String value) {
        this.valueMap.put(key, value);
        return this;
    }

}
