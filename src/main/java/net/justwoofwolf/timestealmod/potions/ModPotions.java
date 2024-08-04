package net.justwoofwolf.timestealmod.potions;

import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.effect.ModEffects;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {

    public static final Potion SLOW_DOWN_POTION =
            Registry.register(Registries.POTION, new Identifier(TimestealMod.MOD_ID, "slow_down_potion"),
                    new Potion(new StatusEffectInstance(ModEffects.SLOW_DOWN, 3600, 0),
                    new StatusEffectInstance(ModEffects.SATURATION_UPDATE, 3600, 0),
                    new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 2),
                    new StatusEffectInstance(StatusEffects.SLOWNESS, 3600, 2),
                    new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 3600, 2)));
    public static final Potion SPEED_UP_POTION =
            Registry.register(Registries.POTION, new Identifier(TimestealMod.MOD_ID, "speed_up_potion"),
                    new Potion(new StatusEffectInstance(ModEffects.SPEED_UP, 3600, 0),
                            new StatusEffectInstance(StatusEffects.SPEED, 3600, 2),
                            new StatusEffectInstance(StatusEffects.REGENERATION, 3600, 1),
                            new StatusEffectInstance(StatusEffects.HASTE, 3600, 2),
                            new StatusEffectInstance(StatusEffects.HUNGER, 3600, 2)));

    public static void RegisterPotions() {
        TimestealMod.LOGGER.info("Registering Potions for " + TimestealMod.MOD_ID);

        RegisterPotionsRecipes();
    }

    public static void RegisterPotionsRecipes() {
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, ModItems.OVERTIME, ModPotions.SPEED_UP_POTION);
        BrewingRecipeRegistry.registerPotionRecipe(ModPotions.SPEED_UP_POTION, Items.FERMENTED_SPIDER_EYE, ModPotions.SLOW_DOWN_POTION);
    }
}
