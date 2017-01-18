package com.ipixelmon.permission;

import com.google.common.collect.Lists;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class PermissionConfig {

    private File file;

    public PermissionConfig(File file) {
        this.file = file;
    }

    private String jsonToString(JSONObject object) {
        if(object == null) {
            return "null";
        } else {
            StringBuffer var1 = new StringBuffer();
            boolean var2 = true;
            Iterator var3 = object.entrySet().iterator();
            var1.append('{');
            var1.append('\n');

            while(var3.hasNext()) {
                if(var2) {
                    var2 = false;
                } else {
                    var1.append(',');
                    var1.append('\n');
                }

                Map.Entry var4 = (Map.Entry)var3.next();
                toJSONString(String.valueOf(var4.getKey()), var4.getValue(), var1);
            }

            var1.append('}');
            return var1.toString();
        }
    }

    private static String toJSONString(String var0, Object var1, StringBuffer var2) {
        var2.append('\"');
        if(var0 == null) {
            var2.append("null");
        } else {
            escape(var0, var2);
        }

        var2.append('\"').append(':');
        var2.append(JSONValue.toJSONString(var1));
        return var2.toString();
    }

    private static void escape(String var0, StringBuffer var1) {
        for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var3 = var0.charAt(var2);
            switch(var3) {
                case '\b':
                    var1.append("\\b");
                    continue;
                case '\t':
                    var1.append("\\t");
                    continue;
                case '\n':
                    var1.append("\\n");
                    continue;
                case '\f':
                    var1.append("\\f");
                    continue;
                case '\r':
                    var1.append("\\r");
                    continue;
                case '\"':
                    var1.append("\\\"");
                    continue;
                case '/':
                    var1.append("\\/");
                    continue;
                case '\\':
                    var1.append("\\\\");
                    continue;
            }

            if(var3 >= 0 && var3 <= 31 || var3 >= 127 && var3 <= 159 || var3 >= 8192 && var3 <= 8447) {
                String var4 = Integer.toHexString(var3);
                var1.append("\\u");

                for(int var5 = 0; var5 < 4 - var4.length(); ++var5) {
                    var1.append('0');
                }

                var1.append(var4.toUpperCase());
            } else {
                var1.append(var3);
            }
        }

    }

    public void write(JSONObject object) {
        try {
            List<String> toWrite = Lists.newArrayList();
            toWrite.add(jsonToString(object));
            Files.write(file.toPath(), toWrite, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject read() {
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(file);
            return (JSONObject) parser.parse(fileReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
