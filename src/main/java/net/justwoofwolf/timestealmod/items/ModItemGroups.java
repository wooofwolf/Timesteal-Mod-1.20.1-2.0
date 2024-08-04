package net.justwoofwolf.timestealmod.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.potions.ModPotions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    private static final ItemGroup TIMESTEAL_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.OVERTIME))
            .displayName(Text.translatable("itemgroup.timestealmod.timesteal_group"))
            .entries(((displayContext, entries) -> {
                entries.add(ModItems.START_SEASON_ITEM);
                entries.add(ModItems.RUSTY_COG);
                entries.add(ModItems.COG);
                entries.add(ModItems.GEAR);
                entries.add(ModItems.GOLDEN_GEAR);
                entries.add(ModItems.WARDEN_HEART);
                entries.add(ModItems.NETHERITE_CLOCK);
                entries.add(ModItems.CUCKOO_CLOCK);
                entries.add(ModItems.SUNDIAL);
                entries.add(ModItems.OVERTIME);
                entries.add(ModItems.GOLDEN_OVERTIME);
                entries.add(ModItems.CLOCK_HAND_ARROW);
                entries.add(ModItems.TOTEM_OF_TIME);
                entries.add(ModItems.DAMAGED_STOPWATCH);
                entries.add(ModItems.REWIND_HOURGLASS);
                entries.add(PotionUtil.setPotion(new ItemStack(Items.POTION), ModPotions.SPEED_UP_POTION));
                entries.add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), ModPotions.SPEED_UP_POTION));
                entries.add(PotionUtil.setPotion(new ItemStack(Items.POTION), ModPotions.SLOW_DOWN_POTION));
                entries.add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), ModPotions.SLOW_DOWN_POTION));
            }))
            .build();

    public static void RegisterItemGroups() {
        TimestealMod.LOGGER.info("Registering Item Groups for " + TimestealMod.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, new Identifier(TimestealMod.MOD_ID, "timesteal_group"), TIMESTEAL_GROUP);
    }
}
