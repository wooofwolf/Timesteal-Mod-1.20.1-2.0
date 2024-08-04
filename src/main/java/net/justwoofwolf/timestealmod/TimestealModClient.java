package net.justwoofwolf.timestealmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.justwoofwolf.timestealmod.entity.ModEntities;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.client.render.entity.ClockHandArrowEntityRenderer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TimestealModClient implements ClientModInitializer {

    public static PlayerData playerData = new PlayerData();
    public static boolean seasonStarted = false;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CLOCK_HAND_ARROW_PROJECTILE, ClockHandArrowEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(TimestealMod.TIME_LEFT, (client, handler, buf, responseSender) -> {
            long timeLeft = buf.readLong();

            client.execute(() -> {
                assert client.player != null;
                client.player.sendMessage(Text.literal(String.format("%d:%02d:%02d", timeLeft / 72000, (timeLeft % 72000) / 1200, timeLeft / 20 % 60))
                        .fillStyle(Style.EMPTY.withBold(true).withColor(Formatting.RED)), true);
           });
        });

        ClientPlayNetworking.registerGlobalReceiver(TimestealMod.SEASON_STARTED, (client, handler, buf, responseSender) -> {
            seasonStarted = buf.readBoolean();

            client.execute(() -> {
                if (!seasonStarted) return;

                assert client.player != null;
                client.player.getWorld().playSound(null, client.player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(TimestealMod.INITIAL_SYNC, (client, handler, buf, responseSender) -> {
            seasonStarted = buf.readBoolean();
            playerData.timeLeft = buf.readLong();

            client.execute(() -> {
                if (!seasonStarted) return;

                assert client.player != null;
                client.player.sendMessage(Text.literal(String.format("%d:%02d:%02d",playerData.timeLeft / 72000, (playerData.timeLeft % 72000) / 1200, playerData.timeLeft / 20 % 60))
                        .fillStyle(Style.EMPTY.withBold(true).withColor(Formatting.RED)), true);

            });
        });
    }
}
