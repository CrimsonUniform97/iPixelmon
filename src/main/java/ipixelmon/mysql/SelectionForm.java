package ipixelmon.mysql;

import java.util.HashMap;
import java.util.Map;

public final class SelectionForm {
    protected Map<String, String> selectionForms;
    protected String tableName;

    public SelectionForm(final String tableName) {
        this.tableName = tableName;
        selectionForms = new HashMap<String, String>();
    }

    public final SelectionForm add(final String key, final String value) {
        this.selectionForms.put(key, value);
        return this;
    }
}
