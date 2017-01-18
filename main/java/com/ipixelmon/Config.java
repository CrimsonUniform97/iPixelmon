package com.ipixelmon;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class Config {

    protected final File configFile;

    public Config(File parFile) {
        configFile = parFile;

        configFile.getParentFile().mkdirs();

        try {
            if (!configFile.exists()) configFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setString(String parKey, String parValue) {
        set(parKey, parValue);
    }

    public void setBoolean(String parKey, boolean parValue) {
        set(parKey, parValue);
    }

    public void setInt(String parKey, int parValue) {
        set(parKey, parValue);
    }

    public void setDouble(String parKey, double parValue) {
        set(parKey, parValue);
    }

    public void setLong(String parKey, long parValue) {
        set(parKey, parValue);
    }

    public String getString(String parKey) {
        return getValue(parKey);
    }

    public boolean getBoolean(String parKey) {
        return Boolean.parseBoolean(getValue(parKey));
    }

    public int getInt(String parKey) {
        return Integer.parseInt(getValue(parKey));
    }

    public double getDouble(String parKey) {
        return Double.parseDouble(getValue(parKey));
    }

    public long getLong(String parKey) {
        return Long.parseLong(getValue(parKey));
    }

    public boolean hasKey(String parKey) {
        return getValue(parKey) != null;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = Maps.newHashMap();

        try {
            Scanner scanner = new Scanner(configFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("=")) {
                    if(line.split("=").length > 1) {
                        map.put(line.split("=")[0], line.split("=")[1]);
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    protected void set(String parKey, Object parValue) {
        try {
            Scanner scanner = new Scanner(configFile);

            List<String> listToReturn = new ArrayList<String>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                listToReturn.add(line);
            }
            scanner.close();

            Iterator iterator = listToReturn.listIterator();
            String s;
            while(iterator.hasNext()) {
                s = (String) iterator.next();

                if(s.split("\\=")[0].equalsIgnoreCase(parKey)) {
                    iterator.remove();
                }
            }

            listToReturn.add(parKey + "=" + parValue);

            Files.write(configFile.toPath(), listToReturn, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getValue(String parKey) {
        try {
            Scanner scanner = new Scanner(configFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("=")) if(line.split("=").length > 1) if (line.split("=")[0].equalsIgnoreCase(parKey)) return line.split("=")[1];
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
