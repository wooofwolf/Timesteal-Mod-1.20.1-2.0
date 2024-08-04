package net.justwoofwolf.timestealmod.items.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.justwoofwolf.timestealmod.gui.DamagedStopwatchGui;
import net.justwoofwolf.timestealmod.gui.RewindHourglassGui;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.justwoofwolf.timestealmod.utils.heads.HeadCache;
import net.justwoofwolf.timestealmod.utils.heads.TextureUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RewindHourglassItem extends Item {

    public RewindHourglassItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));

        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(Objects.requireNonNull(world.getServer()));
        if (!serverState.seasonStarted) return TypedActionResult.fail(user.getStackInHand(hand));

        SimpleGui gui = new RewindHourglassGui(ScreenHandlerType.GENERIC_9X3, (ServerPlayerEntity) user, false);
        gui.setTitle(Text.literal("Rewind a Player"));
        setHeads(gui, world);
        gui.open();

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private void setHeads(SimpleGui gui, World world) {
        PlayerManager manager = Objects.requireNonNull(world.getServer()).getPlayerManager();

        BannedPlayerList bannedPlayerList = manager.getUserBanList();

        try {
            JsonArray array = (JsonArray) JsonParser.parseReader(new FileReader(bannedPlayerList.getFile()));
            for (int i = 0; i < array.size(); ++i) {
                Object o = array.get(i);

                JsonObject player = (JsonObject) o;
                String uuid = String.valueOf(player.get("uuid")).substring(1, player.get("uuid").toString().length() - 1);
                String name = String.valueOf(player.get("name")).substring(1, player.get("name").toString().length() - 1);

                GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
                String value = HeadCache.profiles.get(profile);

                ItemStack head = Items.PLAYER_HEAD.getDefaultStack();
                if (value != null) {
                    head.setNbt(TextureUtils.nbtFromTextureValue(profile.getId(), value, name));
                }
                head.setCustomName(Text.literal(name));

                gui.setSlot(i, GuiElementBuilder.from(head));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
