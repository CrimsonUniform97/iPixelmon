package ipixelmon;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Config {

    private final File configFile;

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

    private void set(String parKey, Object parValue) {
        try {
            if(getValue(parKey) != null) return;

            Scanner scanner = new Scanner(configFile);

            List<String> listToReturn = new ArrayList<String>();
            listToReturn.add(parKey + "=" + parValue);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                listToReturn.add(line);
            }
            scanner.close();

            Files.write(configFile.toPath(), listToReturn, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getValue(String parKey) {
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
