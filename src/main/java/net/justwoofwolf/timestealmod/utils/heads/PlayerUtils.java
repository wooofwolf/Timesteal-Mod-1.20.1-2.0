package net.justwoofwolf.timestealmod.utils.heads;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.UUID;

public class PlayerUtils {
    public static HashMap<String, String> javaUUIDmap = new HashMap<>();

    private static final String MOJANG_SESSIONSERVER = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String MOJANG_UUID = "https://api.mojang.com/users/profiles/minecraft/";

    public static String getJavaUUID(String playername) {
        String lowerCasePlayerName = playername.toLowerCase();

        // cache the UUIDs, so we don't have to make a web request every time
        if (javaUUIDmap.get(lowerCasePlayerName) != null) {
            return javaUUIDmap.get(lowerCasePlayerName);
        }
        JsonObject getJson = new WebUtil().webRequest(MOJANG_UUID + lowerCasePlayerName);

        var uuid = getJson.get("id");
        if (uuid == null) {
            return null;
        }
        return uuid.getAsString();
    }

    public static UUID toUUID(String target) {
        return UUID.fromString(target.replaceAll(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"
        ));
    }

    public static String getTextureID(String lookupID) {
        JsonObject getJson = new WebUtil().webRequest(MOJANG_SESSIONSERVER + lookupID);
        try {
            return getJson.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        } catch (Exception e) {
            return null;
        }
    }
}
