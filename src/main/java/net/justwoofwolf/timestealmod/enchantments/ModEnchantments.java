package net.justwoofwolf.timestealmod.enchantments;

import net.justwoofwolf.timestealmod.TimestealMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static Enchantment TIME_STEAL = new TimeStealEnchantment();

    public static void RegisterModEnchantments() {
        TimestealMod.LOGGER.info("Registering Enchantments for " + TimestealMod.MOD_ID);

        Registry.register(Registries.ENCHANTMENT, new Identifier(TimestealMod.MOD_ID, "time_steal"), TIME_STEAL);
    }
}
