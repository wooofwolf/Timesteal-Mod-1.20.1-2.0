package net.justwoofwolf.timestealmod.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class ServerEntityCombatHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity{
    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (!(entity instanceof ServerPlayerEntity)) return;
        if (!(killedEntity instanceof  ServerPlayerEntity)) return;
        if (killedEntity == entity) return;

        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
        if (!serverState.seasonStarted) return;

        PlayerData playerState = StateSaverAndLoader.getPlayerState((LivingEntity) entity);
        if (playerState.timeLeft + 720000L <= 10800000L) {
            playerState.timeLeft += 720000L;
        }
        else {
            playerState.timeLeft = Math.max(playerState.timeLeft, 10800000L);
        }

        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(playerState.timeLeft);

        Objects.requireNonNull(entity.getServer()).execute(() -> ServerPlayNetworking.send((ServerPlayerEntity) entity, TimestealMod.TIME_LEFT, data));

        if (playerState.timeLeft >= 10800000L) {
            entity.sendMessage(Text.literal("You've hit the cap.")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
            world.playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.MASTER, 1f, 1f);
        }
        else {
            entity.sendMessage(Text.literal("You killed someone and gained 10 hours!")
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1f, 1f);
        }
    }
}
