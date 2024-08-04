package net.justwoofwolf.timestealmod.events;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LootTableHandler implements LootTableEvents.Modify{
    private static final Identifier ABANDONED_MINESHAFT_LOOT_TABLE_ID = new Identifier("minecraft", "chests/abandoned_mineshaft");
    private static final Identifier ANCIENT_CITY_LOOT_TABLE_ID = new Identifier("minecraft", "chests/ancient_city");
    private static final Identifier BASTION_BRIDGE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_bridge");
    private static final Identifier BASTION_HOGLIN_STABLE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_hoglin_stable");
    private static final Identifier BASTION_OTHER_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_other");
    private static final Identifier BASTION_TREASURE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_treasure");
    private static final Identifier BURIED_TREASURE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/buried_treasure");
    private static final Identifier DESERT_PYRAMID_LOOT_TABLE_ID = new Identifier("minecraft", "chests/desert_pyramid");
    private static final Identifier END_CITY_TREASURE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/end_city_treasure");
    private static final Identifier JUNGLE_TEMPLE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/jungle_temple");
    private static final Identifier NETHER_BRIDGE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/nether_bridge");
    private static final Identifier PILLAGER_OUTPOST_LOOT_TABLE_ID = new Identifier("minecraft", "chests/pillager_outpost");
    private static final Identifier SHIPWRECK_TREASURE_LOOT_TABLE_ID = new Identifier("minecraft", "chests/shipwreck_treasure");
    private static final Identifier SIMPLE_DUNGEON_LOOT_TABLE_ID = new Identifier("minecraft", "chests/simple_dungeon");
    private static final Identifier STRONGHOLD_CORRIDOR_LOOT_TABLE_ID = new Identifier("minecraft", "chests/stronghold_corridor");
    private static final Identifier STRONGHOLD_CROSSING_LOOT_TABLE_ID = new Identifier("minecraft", "chests/stronghold_crossing");
    private static final Identifier STRONGHOLD_LIBRARY_LOOT_TABLE_ID = new Identifier("minecraft", "chests/stronghold_library");
    private static final Identifier WOODLAND_MANSION_LOOT_TABLE_ID = new Identifier("minecraft", "chests/woodland_mansion");

    private static final Identifier wardenLoot = new Identifier("minecraft", "entities/warden");

    private static final List<Identifier> easyLoot = Arrays.asList(ABANDONED_MINESHAFT_LOOT_TABLE_ID,
            BURIED_TREASURE_LOOT_TABLE_ID,
            DESERT_PYRAMID_LOOT_TABLE_ID,
            JUNGLE_TEMPLE_LOOT_TABLE_ID,
            SHIPWRECK_TREASURE_LOOT_TABLE_ID,
            SIMPLE_DUNGEON_LOOT_TABLE_ID);

    private static final List<Identifier> mediumLoot = Arrays.asList(BASTION_BRIDGE_LOOT_TABLE_ID,
            BASTION_HOGLIN_STABLE_LOOT_TABLE_ID,
            BASTION_OTHER_LOOT_TABLE_ID,
            BASTION_TREASURE_LOOT_TABLE_ID,
            NETHER_BRIDGE_LOOT_TABLE_ID,
            PILLAGER_OUTPOST_LOOT_TABLE_ID);

    private static final List<Identifier> hardLoot = Arrays.asList(ANCIENT_CITY_LOOT_TABLE_ID,
            END_CITY_TREASURE_LOOT_TABLE_ID,
            STRONGHOLD_CORRIDOR_LOOT_TABLE_ID,
            STRONGHOLD_CROSSING_LOOT_TABLE_ID,
            STRONGHOLD_LIBRARY_LOOT_TABLE_ID,
            WOODLAND_MANSION_LOOT_TABLE_ID);

    @Override
    public void modifyLootTable(ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) {
        if (!source.isBuiltin()) return;

        if (wardenLoot.equals(id)) {
            LootPool.Builder poolBuilder = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(RandomChanceLootCondition.builder(0.5f))
                    .with(ItemEntry.builder(ModItems.WARDEN_HEART)
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))));
            tableBuilder.pool(poolBuilder.build());
            return;
        }

        for (Identifier identifier : easyLoot) {
            if (identifier.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.RUSTY_COG)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f))));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.COG)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f))));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.5f))
                        .with(ItemEntry.builder(ModItems.GEAR)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 1.0f))));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.0025f))
                        .with(ItemEntry.builder(ModItems.GOLDEN_GEAR)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))));
                tableBuilder.pool(poolBuilder.build());
            }
        }
        for (Identifier identifier : mediumLoot) {
            if (identifier.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.RUSTY_COG))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.COG))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 3.0f)));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.GEAR)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 2.0f))));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.005f))
                        .with(ItemEntry.builder(ModItems.GOLDEN_GEAR)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))));
                tableBuilder.pool(poolBuilder.build());
            }
        }
        for (Identifier identifier : hardLoot) {
            if (identifier.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.RUSTY_COG))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 5.0f)));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.COG))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f)));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.GEAR)
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.0f, 3.0f))));
                tableBuilder.pool(poolBuilder.build());

                poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.01f))
                        .with(ItemEntry.builder(ModItems.GOLDEN_GEAR)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f))));
                tableBuilder.pool(poolBuilder.build());
            }
        }
    }
}
