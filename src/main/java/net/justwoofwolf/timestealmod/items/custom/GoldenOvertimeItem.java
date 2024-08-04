package net.justwoofwolf.timestealmod.items.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class GoldenOvertimeItem extends Item {

    public GoldenOvertimeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));

        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(Objects.requireNonNull(world.getServer()));
        if (!serverState.seasonStarted) return TypedActionResult.fail(user.getStackInHand(hand));

        PlayerData playerState = StateSaverAndLoader.getPlayerState(user);
        playerState.timeLeft = playerState.timeLeft + 7200000L;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(playerState.timeLeft);

        Objects.requireNonNull(user.getServer()).execute(() -> ServerPlayNetworking.send((ServerPlayerEntity) user, TimestealMod.TIME_LEFT, data));

        user.sendMessage(Text.literal("You gained 100 hours!")
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1f, 1f);

        if (!user.isCreative()) {
            user.getStackInHand(hand).decrement(1);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Use when the Time is right...").setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE).withItalic(true)));
    }
}
