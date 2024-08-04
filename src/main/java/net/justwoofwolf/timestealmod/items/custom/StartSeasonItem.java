package net.justwoofwolf.timestealmod.items.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StartSeasonItem extends Item {

    public StartSeasonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));

        MinecraftServer server = world.getServer();

        assert server != null;
        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
        serverState.seasonStarted = true;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeBoolean(serverState.seasonStarted);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            server.execute(() -> ServerPlayNetworking.send(player, TimestealMod.SEASON_STARTED, data));

            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 1f, 1f);
        }

        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                "/title @a subtitle {\"text\":\"Time waits for no one...\",\"color\":\"dark_red\"}");
        server.getCommandManager().executeWithPrefix(server.getCommandSource(),
                "/title @a title [\"\",{\"text\":\"abc\",\"obfuscated\":true,\"color\":\"red\"},{\"text\":\" Good Luck\",\"bold\":true,\"color\":\"red\"},{\"text\":\" abc\",\"obfuscated\":true,\"color\":\"red\"}]");

        if (!user.isCreative()) {
            user.getStackInHand(hand).decrement(1);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
