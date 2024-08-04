package net.justwoofwolf.timestealmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SeasonCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("season")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("The arguments are not specified correctly."), false);
                    return 1;
                })
                .then(CommandManager.literal("start")
                    .executes(context -> {
                        MinecraftServer server = context.getSource().getServer();
                        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
                        serverState.seasonStarted = true;

                        PacketByteBuf data = PacketByteBufs.create();
                        data.writeBoolean(serverState.seasonStarted);
                        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                            server.execute(() -> ServerPlayNetworking.send(player, TimestealMod.SEASON_STARTED, data));
                        }

                        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                                "/title @a subtitle {\"text\":\"Time waits for no one...\",\"color\":\"dark_red\"}");
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                                "/title @a title [\"\",{\"text\":\"abc\",\"obfuscated\":true,\"color\":\"red\"},{\"text\":\" Good Luck\",\"bold\":true,\"color\":\"red\"},{\"text\":\" abc\",\"obfuscated\":true,\"color\":\"red\"}]");

                        context.getSource().sendFeedback(() -> Text.literal("Season has started successfully."), false);

                        return 1;
                    }))
                .then(CommandManager.literal("stop")
                    .executes(context -> {
                        MinecraftServer server = context.getSource().getServer();
                        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
                        serverState.seasonStarted = false;

                        PacketByteBuf data = PacketByteBufs.create();
                        data.writeBoolean(serverState.seasonStarted);
                        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                            server.execute(() -> ServerPlayNetworking.send(player, TimestealMod.SEASON_STARTED, data));
                        }

                        context.getSource().sendFeedback(() -> Text.literal("Season has stopped successfully."), false);

                        return 1;
                    })));
    }
}
