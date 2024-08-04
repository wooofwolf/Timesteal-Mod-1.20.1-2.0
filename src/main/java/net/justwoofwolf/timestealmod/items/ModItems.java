package net.justwoofwolf.timestealmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.items.custom.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item START_SEASON_ITEM =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "start_season"),
                    new StartSeasonItem(new FabricItemSettings()));
    public static final Item RUSTY_COG =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "rusty_cog"),
                    new Item(new FabricItemSettings()));
    public static final Item COG =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "cog"),
                    new Item(new FabricItemSettings()));
    public static final Item GEAR =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "gear"),
                    new Item(new FabricItemSettings()));
    public static final Item GOLDEN_GEAR =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "golden_gear"),
                    new GoldenGearItem(new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final Item WARDEN_HEART =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "warden_heart"),
                    new Item(new FabricItemSettings().food(ModFoodComponents.WARDEN_HEART)));
    public static final Item NETHERITE_CLOCK =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "netherite_clock"),
                    new Item(new FabricItemSettings()));
    public static final Item CUCKOO_CLOCK =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "cuckoo_clock"),
                    new CuckooClockItem(new FabricItemSettings()));
    public static final Item SUNDIAL =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "sundial"),
                    new SundialItem(new FabricItemSettings().maxDamage(4)));
    public static final Item OVERTIME =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "overtime"),
                    new OvertimeItem(new FabricItemSettings()));
    public static final Item GOLDEN_OVERTIME =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "golden_overtime"),
                    new GoldenOvertimeItem(new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final Item WITHDRAWN_TIME =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "withdrawn_time"),
                    new WithdrawnTimeItem(new FabricItemSettings()));
    public static final Item WITHDRAWN_TIME_FIFTY =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "withdrawn_time_fifty"),
                    new WithdrawnTimeFiftyItem(new FabricItemSettings()));
    public static final Item CLOCK_HAND_ARROW =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "clock_hand_arrow"),
                    new ClockHandArrowItem(new FabricItemSettings()));
    public static final Item TOTEM_OF_TIME =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "totem_of_time"),
                    new Item(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON)));
    public static final Item DAMAGED_STOPWATCH =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "damaged_stopwatch"),
                    new DamagedStopwatchItem(new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item REWIND_HOURGLASS =
            Registry.register(Registries.ITEM, new Identifier(TimestealMod.MOD_ID, "rewind_hourglass"),
                    new RewindHourglassItem(new FabricItemSettings().rarity(Rarity.UNCOMMON)));

    public static void RegisterModItems() {
        TimestealMod.LOGGER.info("Registering Mod Items for " + TimestealMod.MOD_ID);
    }
}
