package net.justwoofwolf.timestealmod.items.custom;

import com.mojang.authlib.GameProfile;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.gui.DamagedStopwatchGui;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.justwoofwolf.timestealmod.utils.heads.HeadCache;
import net.justwoofwolf.timestealmod.utils.heads.TextureUtils;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class DamagedStopwatchItem extends Item {

    public DamagedStopwatchItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));

        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(Objects.requireNonNull(world.getServer()));
        if (!serverState.seasonStarted) return TypedActionResult.fail(user.getStackInHand(hand));

        SimpleGui gui = new DamagedStopwatchGui(ScreenHandlerType.GENERIC_9X3, (ServerPlayerEntity) user, false);
        gui.setTitle(Text.literal("Who's Time is up?"));
        setHeads(gui, world);
        gui.open();

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    private void setHeads(SimpleGui gui, World world) {
        PlayerManager manager = Objects.requireNonNull(world.getServer()).getPlayerManager();

        List<ServerPlayerEntity> playerList = manager.getPlayerList();

        for (int i = 0; i < playerList.size(); ++i) {
            ServerPlayerEntity o = playerList.get(i);

            UUID uuid = o.getUuid();
            String name = o.getName().getString();

            GameProfile profile = new GameProfile(uuid, name);
            String value = HeadCache.profiles.get(profile);

            ItemStack head = Items.PLAYER_HEAD.getDefaultStack();
            if (value != null) {
                head.setNbt(TextureUtils.nbtFromTextureValue(profile.getId(), value, name));
            }
            head.setCustomName(Text.literal(name));

            gui.setSlot(i, GuiElementBuilder.from(head));
        }
    }
}
