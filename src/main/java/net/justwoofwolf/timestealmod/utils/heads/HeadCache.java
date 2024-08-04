package net.justwoofwolf.timestealmod.utils.heads;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.Whitelist;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeadCache {
    public static boolean registered = false;
    public static Map<GameProfile, String> profiles = new HashMap<>();

    public static void registerProfiles(MinecraftServer server) {
        if (server.isSingleplayer()) {
            GameProfile player = server.getHostProfile();

            String value = PlayerUtils.getTextureID(player.getId().toString());
            profiles.put(player, value);
        }
        else {
            PlayerManager manager = server.getPlayerManager();
            Whitelist whitelist = manager.getWhitelist();

            try {
                JsonArray array = (JsonArray) JsonParser.parseReader(new FileReader(whitelist.getFile()));
                for (int i = 0; i < array.size(); ++i) {
                    Object o = array.get(i);

                    JsonObject player = (JsonObject) o;
                    String uuid = String.valueOf(player.get("uuid")).substring(1, player.get("uuid").toString().length() - 1);
                    String name = String.valueOf(player.get("name")).substring(1, player.get("name").toString().length() - 1);

                    String value = PlayerUtils.getTextureID(uuid);

                    profiles.put(new GameProfile(UUID.fromString(uuid), name), value);
                }
            } catch (FileNotFoundException e) {
                TimestealMod.LOGGER.info("Profile Registering FAILED");
                throw new RuntimeException(e);
            }
        }

        TimestealMod.LOGGER.info("Profile Registering SUCCEEDED");
        registered = true;
    }
}
