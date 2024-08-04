package net.justwoofwolf.timestealmod.entity.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.entity.ModEntities;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Random;

public class ClockHandArrowProjectileEntity extends PersistentProjectileEntity {
    public ClockHandArrowProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ClockHandArrowProjectileEntity(World world, double x, double y, double z) {
        super(ModEntities.CLOCK_HAND_ARROW_PROJECTILE, x, y, z, world);
    }

    public ClockHandArrowProjectileEntity(World world, LivingEntity owner) {
        super(ModEntities.CLOCK_HAND_ARROW_PROJECTILE, owner, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.CLOCK_HAND_ARROW);
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected void onHit(LivingEntity target) {
        // Reduce time
        if (target instanceof ServerPlayerEntity) {
            StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(Objects.requireNonNull(target.getServer()));
            if (serverState.seasonStarted) {
                ServerPlayerEntity player = (ServerPlayerEntity) target;
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                playerState.timeLeft -= 600L;

                PacketByteBuf data = PacketByteBufs.create();
                data.writeLong(playerState.timeLeft);

                Objects.requireNonNull(player.getServer()).execute(() -> ServerPlayNetworking.send(player, TimestealMod.TIME_LEFT, data));

                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 1f, 1f);
            }
        }

        super.onHit(target);
    }
}
