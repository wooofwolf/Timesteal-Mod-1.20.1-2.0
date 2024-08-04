package net.justwoofwolf.timestealmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.justwoofwolf.timestealmod.commands.ModCommands;
import net.justwoofwolf.timestealmod.effect.ModEffects;
import net.justwoofwolf.timestealmod.enchantments.ModEnchantments;
import net.justwoofwolf.timestealmod.entity.ModEntities;
import net.justwoofwolf.timestealmod.events.*;
import net.justwoofwolf.timestealmod.items.ModItemGroups;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.justwoofwolf.timestealmod.potions.ModPotions;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimestealMod implements ModInitializer {
	public static final String MOD_ID = "timestealmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier TIME_LEFT = new Identifier(MOD_ID, "time_left");

	public static final Identifier SEASON_STARTED = new Identifier(MOD_ID, "season_started");

	public static final Identifier INITIAL_SYNC = new Identifier(MOD_ID, "initial_sync");

	@Override
	public void onInitialize() {
		ModItems.RegisterModItems();
		ModItemGroups.RegisterItemGroups();
		ModEntities.RegisterModEntities();
		ModEffects.RegisterEffects();
		ModPotions.RegisterPotions();
		//ModEnchantments.RegisterModEnchantments();

		ModCommands.RegisterCommands();

		ServerPlayConnectionEvents.JOIN.register(new ServerPlayConnectionHandler());
		ServerTickEvents.END_SERVER_TICK.register(new ServerTickHandler());
		ServerLivingEntityEvents.ALLOW_DEATH.register(new ServerLivingEntityHandler());
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new ServerEntityCombatHandler());
		LootTableEvents.MODIFY.register(new LootTableHandler());
	}
}