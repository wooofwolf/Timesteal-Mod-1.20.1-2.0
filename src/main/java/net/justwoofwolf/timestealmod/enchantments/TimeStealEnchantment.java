package net.justwoofwolf.timestealmod.enchantments;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class TimeStealEnchantment extends Enchantment {
    protected TimeStealEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        MinecraftServer server = user.getServer();
        assert server != null;
        StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
        if (!serverState.seasonStarted) return;
        if (!user.getWorld().isClient) return;
        if (!target.isPlayer()) return;

        PlayerData playerState = StateSaverAndLoader.getPlayerState((LivingEntity) target);
        playerState.timeLeft -= 100L;

        PacketByteBuf data = PacketByteBufs.create();
        data.writeLong(playerState.timeLeft);

        Objects.requireNonNull(target.getServer()).execute(() -> ServerPlayNetworking.send((ServerPlayerEntity) target, TimestealMod.TIME_LEFT, data));

        super.onTargetDamaged(user, target, level);
    }
}
