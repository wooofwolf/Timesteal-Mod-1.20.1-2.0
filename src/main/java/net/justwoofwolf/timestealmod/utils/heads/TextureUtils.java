package net.justwoofwolf.timestealmod.utils.heads;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;

import java.util.UUID;

public class TextureUtils {

    public static NbtCompound nbtFromTextureValue(UUID id, String texturevalue, String shownname) {
        NbtCompound nbtCompound = new NbtCompound();
        NbtCompound skullownertag = new NbtCompound();
        NbtCompound texturetag = new NbtCompound();
        NbtList texturelist = new NbtList();
        NbtCompound valuetag = new NbtCompound();
        NbtCompound displaytag = new NbtCompound();

        valuetag.putString("Value", texturevalue);
        texturelist.add(valuetag);
        texturetag.put("textures", texturelist);
        skullownertag.put("Properties", texturetag);
        skullownertag.putUuid("Id", id);
        nbtCompound.put("SkullOwner", skullownertag);
        displaytag.putString("Name", shownname);
        nbtCompound.put("display", displaytag);

        return nbtCompound;
    }

    public static NbtCompound nbtFromProfile(GameProfile profile) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile));

        NbtCompound displaytag = new NbtCompound();
        displaytag.putString("Name", getHeadName(nameFromProfile(profile)));
        nbtCompound.put("display", displaytag);

        return nbtCompound;
    }

    private static String nameFromProfile(GameProfile profile) {
        return profile.getName();
    }

    private static String getHeadName(String name) {
        return name;
    }
}