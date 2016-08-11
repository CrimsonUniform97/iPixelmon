package ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class DeleteForm {

    protected Map<String, String> deleteForms;
    protected String tableName;

    public DeleteForm(final String tableName) {
        this.tableName = tableName;
        deleteForms = new HashMap<String, String>();
    }

    public final DeleteForm add(final String key, final String value) {
        this.deleteForms.put(key, value);
        return this;
    }

}
