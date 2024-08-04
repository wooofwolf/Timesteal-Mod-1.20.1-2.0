package net.justwoofwolf.timestealmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class WithdrawCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("withdraw")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal("The arguments are not specified correctly."), false);
                    return 1;
                })
                .then(CommandManager.argument("hours", IntegerArgumentType.integer(1))
                    .executes(context -> {
                        MinecraftServer server = context.getSource().getServer();
                        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
                        if (!serverState.seasonStarted) return -1;

                        ServerPlayerEntity player = context.getSource().getPlayer();
                        PlayerData playerState = StateSaverAndLoader.getPlayerState(Objects.requireNonNull(player));
                        long hoursInTicks = IntegerArgumentType.getInteger(context, "hours") * 72000L;
                        if (hoursInTicks > playerState.timeLeft) {
                            player.sendMessage(Text.literal("Time withdrawn cannot be more than current amount of time.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                            return -1;
                        }

                        playerState.timeLeft -= hoursInTicks;

                        PacketByteBuf data = PacketByteBufs.create();
                        data.writeLong(playerState.timeLeft);
                        server.execute(() -> ServerPlayNetworking.send(player, TimestealMod.TIME_LEFT, data));

                        player.sendMessage(Text.literal("You withdrew " + hoursInTicks / 72000L + " hour(s)!").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
                        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.MASTER, 1f, 1f);

                        int hours = (int) (hoursInTicks / 72000L);
                        ItemStack withdrawnTime;
                        if (hours == 50) {
                            withdrawnTime = ModItems.WITHDRAWN_TIME_FIFTY.getDefaultStack();
                        }
                        else {
                            withdrawnTime = ModItems.WITHDRAWN_TIME.getDefaultStack();
                        }
                        NbtCompound nbt = new NbtCompound();
                        nbt.putInt("hours", hours);
                        withdrawnTime.setNbt(nbt);

                        player.giveItemStack(withdrawnTime);

                        return 1;
                    })));
    }
}
