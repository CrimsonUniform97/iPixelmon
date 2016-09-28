package com.ipixelmon.uuidmanager;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public final class NameFetcher implements Callable<Map<UUID, String>> {
    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private final JSONParser jsonParser = new JSONParser();
    private final List<UUID> uuids;

    public NameFetcher(final List<UUID> uuids) {
        this.uuids = ImmutableList.copyOf(uuids);
    }

    @Override
    public final Map<UUID, String> call() throws Exception {
        final Map<UUID, String> uuidStringMap = new HashMap<UUID, String>();
        for (UUID uuid : uuids) {
            final HttpURLConnection connection = (HttpURLConnection) new URL(PROFILE_URL + uuid.toString().replace("-", "")).openConnection();
            final JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
            final String name = (String) response.get("name");
            if (name == null) {
                continue;
            }
            final String cause = (String) response.get("cause");
            final String errorMessage = (String) response.get("errorMessage");
            if (cause != null && cause.length() > 0) {
                throw new IllegalStateException(errorMessage);
            }
            uuidStringMap.put(uuid, name);
        }

        return uuidStringMap;
    }
}