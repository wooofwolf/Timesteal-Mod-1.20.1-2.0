package net.justwoofwolf.timestealmod.items.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class CuckooClockItem extends Item {

    public CuckooClockItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));

        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(Objects.requireNonNull(world.getServer()));
        if (!serverState.seasonStarted) return TypedActionResult.fail(user.getStackInHand(hand));

        Random r = new Random();
        long timeChange = r.nextLong(-72000L, 72000L);

        PlayerData playerState = StateSaverAndLoader.getPlayerState(user);
        if (playerState.timeLeft + timeChange <= 7200000L || timeChange <= 0) {
            playerState.timeLeft += timeChange;
        }
        else {
            playerState.timeLeft = Math.max(playerState.timeLeft, 7200000L);
        }

        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(playerState.timeLeft);

        Objects.requireNonNull(user.getServer()).execute(() -> ServerPlayNetworking.send((ServerPlayerEntity) user, TimestealMod.TIME_LEFT, data));

        if (playerState.timeLeft >= 7200000L && timeChange > 0) {
            user.sendMessage(Text.literal("You've hit the cap.")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.MASTER, 1f, 1f);
        }
        else if (timeChange > 0) {
            user.sendMessage(Text.literal("You gained time!")
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1f, 1f);
        }
        else if (timeChange < 0) {
            user.sendMessage(Text.literal("You lost time.")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.MASTER, 1f, 1f);
        }
        else {
            user.sendMessage(Text.literal("You somehow got nothing???")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
            world.playSound(null, user.getBlockPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 1f, 1f);
        }

        user.addStatusEffect(new StatusEffectInstance(Objects.requireNonNull(StatusEffect.byRawId(r.nextInt(33 - 1) + 1)),
                r.nextInt(72000 - 1) + 1, r.nextInt(0, 2)));

        if (!user.isCreative()) {
            user.getStackInHand(hand).decrement(1);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
