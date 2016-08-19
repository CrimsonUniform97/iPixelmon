package ipixelmon.mysql;


import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import lib.PatPeter.SQLibrary.Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MySQLHandler {

    private Database database;

    public MySQLHandler(final Database database) {
        this.database = database;
        this.database.open();
    }

    public final Database getDatabase() { return database; }

    public final ResultSet query(final String str) {

        if (str.toUpperCase().contains("CREATE")) {
            System.out.println("Error!!!! Use createTable() to create table.");
            return null;
        }

        try {
            return database.query(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public final void createTable(final Class<? extends IMod> modClass, final CreateForm form) {
        IMod mod = null;

        for(IMod m : iPixelmon.mods) {
            if(m.getClass().getCanonicalName().equalsIgnoreCase(modClass.getCanonicalName())) {
                mod = m;
                break;
            }
        }

        if(mod == null) return;

        try {
            String createStr = "";

            for (String column : form.dataTypeMap.keySet()) {
                if (createStr.isEmpty())
                    createStr += column + " " + form.dataTypeMap.get(column);
                else
                    createStr += ", " + column + " " + form.dataTypeMap.get(column);
            }


            database.query("CREATE TABLE IF NOT EXISTS " + mod.getID() + form.table + "(" + createStr + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void insert(final Class<? extends IMod> modClass, final InsertForm form) {
        IMod mod = null;

        for(IMod m : iPixelmon.mods) {
            if(m.getClass().getCanonicalName().equalsIgnoreCase(modClass.getCanonicalName())) {
                mod = m;
                break;
            }
        }

        if(mod == null) return;

        String keys = "";

        for (String key : form.valueMap.keySet()) {
            keys += keys.isEmpty() ? key : "," + key;
        }

        String values = "";

        for (String value : form.valueMap.values()) {
            values += values.isEmpty() ? "'" + value + "'" : ", " + "'" + value + "'";
        }

        try {
            database.query("INSERT INTO " + mod.getID() + form.tableName + "(" + keys + ") VALUES (" + values + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final ResultSet selectAllFrom(final Class<? extends IMod> modClass, final SelectionForm form) {
        IMod mod = null;

        for(IMod m : iPixelmon.mods) {
            if(m.getClass().getCanonicalName().equalsIgnoreCase(modClass.getCanonicalName())) {
                mod = m;
                break;
            }
        }

        if(mod == null) return null;

        try {
            if (form.selectionForms.isEmpty()) {
                return database.query("SELECT * FROM " + mod.getID() + form.tableName + "" + (form.limit > 0 ? " LIMIT " + form.limit : "") + ";");
            } else {
                String toSearch = "";
                for (String key : form.selectionForms.keySet()) {
                    if (toSearch.isEmpty()) {
                        toSearch += key + "='" + form.selectionForms.get(key) + "'";
                    } else {
                        toSearch += " AND " + key + "='" + form.selectionForms.get(key) + "'";
                    }
                }

                return database.query("SELECT * FROM " + mod.getID() + form.tableName + " WHERE " + toSearch + "" + (form.limit > 0 ? " LIMIT " + form.limit : "") + ";");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public final void update(final Class<? extends IMod> modClass, final UpdateForm updateForm) {
         IMod mod = null;

        for(IMod m : iPixelmon.mods) {
            if(m.getClass().getCanonicalName().equalsIgnoreCase(modClass.getCanonicalName())) {
                mod = m;
                break;
            }
        }

        if(mod == null) return;


        String setStr = null;

        for (String key : updateForm.updateFormsSet.keySet()) {
            if (setStr == null) {
                setStr = key + "='" + updateForm.updateFormsSet.get(key) + "'";
            } else {
                setStr += "," + key + "='" + updateForm.updateFormsSet.get(key) + "'";
            }
        }

        String whereStr = null;
        for (String key : updateForm.updateFormsWhere.keySet()) {
            if (whereStr == null)
                whereStr = key + "='" + updateForm.updateFormsWhere.get(key) + "'";
            else
                whereStr += " AND " + key + "='" + updateForm.updateFormsWhere.get(key) + "'";
        }

        try {
            database.query("UPDATE " + mod.getID() + updateForm.tableName + " SET " + setStr + " WHERE " + whereStr + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public final void delete(final Class<? extends IMod> modClass, final DeleteForm deleteForm) {
        IMod mod = null;

        for(IMod m : iPixelmon.mods) {
            if(m.getClass().getCanonicalName().equalsIgnoreCase(modClass.getCanonicalName())) {
                mod = m;
                break;
            }
        }

        if(mod == null) return;

        String deleteStr = null;

        for(String key : deleteForm.deleteForms.keySet()) {
            if(deleteStr == null)
                deleteStr = key + "='" + deleteForm.deleteForms.get(key) + "'";
            else
                deleteStr += " AND " + key + "='" + deleteForm.deleteForms.get(key) + "'";
        }

        try {
            database.query("DELETE FROM " + mod.getID() + deleteForm.tableName + " WHERE " + deleteStr + ";");
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
