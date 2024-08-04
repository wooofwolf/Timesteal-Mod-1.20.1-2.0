package net.justwoofwolf.timestealmod.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent WARDEN_HEART = new FoodComponent.Builder().hunger(0).saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 2400, 0), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 1), 1.0f).alwaysEdible().build();
}
