package net.justwoofwolf.timestealmod.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class ServerLivingEntityHandler implements ServerLivingEntityEvents.AllowDeath{
    @Override
    public boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if (!(entity instanceof ServerPlayerEntity)) return true;

        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(Objects.requireNonNull(entity.getServer()));
        if (!serverState.seasonStarted) return true;

        if (entity.getMainHandStack().isOf(ModItems.TOTEM_OF_TIME) || entity.getOffHandStack().isOf(ModItems.TOTEM_OF_TIME))
            return true;

        PlayerData playerState = StateSaverAndLoader.getPlayerState(entity);
        playerState.timeLeft -= 720000L;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(playerState.timeLeft);

        Objects.requireNonNull(entity.getServer()).execute(() -> ServerPlayNetworking.send((ServerPlayerEntity) entity, TimestealMod.TIME_LEFT, data));

        entity.sendMessage(Text.literal("You died and lost 10 hours.")
                .setStyle(Style.EMPTY.withColor(Formatting.RED)));
        entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.MASTER, 1f, 1f);

        return true;
    }
}
