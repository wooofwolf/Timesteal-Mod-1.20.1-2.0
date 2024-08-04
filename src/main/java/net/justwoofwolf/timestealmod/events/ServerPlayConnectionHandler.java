package net.justwoofwolf.timestealmod.events;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

public class ServerPlayConnectionHandler implements ServerPlayConnectionEvents.Join{
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        PlayerData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
        PacketByteBuf data = PacketByteBufs.create();
        data.writeBoolean(StateSaverAndLoader.getServerState(server).seasonStarted);

        data.writeLong(playerState.timeLeft);
        server.execute(() -> ServerPlayNetworking.send(handler.getPlayer(), TimestealMod.INITIAL_SYNC, data));
    }
}
