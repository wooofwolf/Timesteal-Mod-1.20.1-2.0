package net.justwoofwolf.timestealmod.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class SaturationUpdateEffect extends StatusEffect {
    public SaturationUpdateEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof PlayerEntity && !pLivingEntity.getWorld().isClient()) {
            if (((PlayerEntity)pLivingEntity).getHungerManager().isNotFull()) {
                ((PlayerEntity)pLivingEntity).getHungerManager().add(pAmplifier + 1, 1.0F);
            }
        }

        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
