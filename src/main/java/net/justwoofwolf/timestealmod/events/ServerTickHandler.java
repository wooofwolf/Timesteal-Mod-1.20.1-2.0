package net.justwoofwolf.timestealmod.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.effect.ModEffects;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.justwoofwolf.timestealmod.utils.heads.HeadCache;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ServerTickHandler implements ServerTickEvents.EndTick{
    @Override
    public void onEndTick(MinecraftServer server) {
        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
        if (!serverState.seasonStarted) return;

        if (!HeadCache.registered) {
            HeadCache.registerProfiles(server);
        }

        // Send a packet to all active clients
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (player == null) continue;

            PlayerData playerState = StateSaverAndLoader.getPlayerState(player);

            if (player.hasStatusEffect(ModEffects.SPEED_UP) && player.hasStatusEffect(ModEffects.SLOW_DOWN)) {
                playerState.timeLeft -= 1L;
            }
            else if (player.hasStatusEffect(ModEffects.SPEED_UP)) {
                playerState.timeLeft -= 20L;
            }
            else if (player.hasStatusEffect(ModEffects.SLOW_DOWN)) {
                if (server.getTicks() % 10 == 0) playerState.timeLeft -= 1L;
            }
            else {
                playerState.timeLeft -= 1L;
            }

            if (playerState.timeLeft <= 0L) {
                playerState.timeLeft = 1728000L;

                // Kick player
                if (server.isSingleplayer()) server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                        "kick ${name} Your Time is up.".replace("${name}", player.getEntityName()));
                else server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                        "ban ${name} Your Time is up.".replace("${name}", player.getEntityName()));
            }

            PacketByteBuf data = PacketByteBufs.create();
            data.writeLong(playerState.timeLeft);

            server.execute(() -> ServerPlayNetworking.send(player, TimestealMod.TIME_LEFT, data));
        }

        if (serverState.sundialTime > 0L) {
            TimeCommand.executeAdd(server.getCommandSource(), 10);
            serverState.sundialTime -= 10;
        }
    }
}
