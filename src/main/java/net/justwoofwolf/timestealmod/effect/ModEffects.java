package net.justwoofwolf.timestealmod.effect;

import net.justwoofwolf.timestealmod.TimestealMod;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static StatusEffect SLOW_DOWN;
    public static StatusEffect SPEED_UP;
    public static StatusEffect SATURATION_UPDATE;

    public static StatusEffect registerStatusEffect(String name) {
        if (name.equals("slow_down")) {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TimestealMod.MOD_ID, name),
                    new SlowDownEffect(StatusEffectCategory.NEUTRAL, 0x3271BF));
        } else if (name.equals("speed_up")){
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TimestealMod.MOD_ID, name),
                    new SpeedUpEffect(StatusEffectCategory.NEUTRAL, 0xF5D049));
        } else {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TimestealMod.MOD_ID, name),
                    new SaturationUpdateEffect(StatusEffectCategory.BENEFICIAL, 16262179));
        }
    }

    public static void RegisterEffects() {
        SLOW_DOWN = registerStatusEffect("slow_down");
        SPEED_UP = registerStatusEffect("speed_up");
        SATURATION_UPDATE = registerStatusEffect("saturation_update");
    }
}
