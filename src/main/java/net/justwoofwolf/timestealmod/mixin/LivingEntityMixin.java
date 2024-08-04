package net.justwoofwolf.timestealmod.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.justwoofwolf.timestealmod.utils.PlayerData;
import net.justwoofwolf.timestealmod.utils.StateSaverAndLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin (LivingEntity.class)
public class LivingEntityMixin {
    @Overwrite()
    private boolean tryUseTotem(DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            ItemStack itemStack = null;
            Hand[] var4 = Hand.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Hand hand = var4[var6];
                ItemStack itemStack2 = entity.getStackInHand(hand);
                if (itemStack2.isOf(ModItems.TOTEM_OF_TIME)) {
                    itemStack = itemStack2.copy();
                    itemStack2.decrement(1);
                    break;
                }
            }

            if (itemStack != null) {
                if (entity instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity;
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                    Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
                }

                entity.setHealth(1.0F);
                entity.clearStatusEffects();
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                entity.getWorld().sendEntityStatus(entity, (byte)35);

                PlayerData playerState = StateSaverAndLoader.getPlayerState(Objects.requireNonNull(entity));
                playerState.timeLeft -= 360000L;

                PacketByteBuf data = PacketByteBufs.create();
                data.writeLong(playerState.timeLeft);
                Objects.requireNonNull(entity.getServer()).execute(() -> {
                    assert entity instanceof ServerPlayerEntity;
                    ServerPlayNetworking.send((ServerPlayerEntity) entity, TimestealMod.TIME_LEFT, data);
                });

                entity.sendMessage(Text.literal("You used a totem and lost 5 hours.")
                        .setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }

            return itemStack != null;
        }
    }
}
