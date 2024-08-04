package net.justwoofwolf.timestealmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.entity.custom.ClockHandArrowProjectileEntity;
import net.justwoofwolf.timestealmod.items.ModItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ModEntities {
    public static final EntityType<ClockHandArrowProjectileEntity> CLOCK_HAND_ARROW_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(TimestealMod.MOD_ID, "clock_hand_arrow_projectile"),
            FabricEntityTypeBuilder.<ClockHandArrowProjectileEntity>create(SpawnGroup.MISC,
            ClockHandArrowProjectileEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static void RegisterModEntities() {
        TimestealMod.LOGGER.info("Registering Mod Entities for " + TimestealMod.MOD_ID);

        DispenserBlock.registerBehavior(ModItems.CLOCK_HAND_ARROW, new ProjectileDispenserBehavior() {
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                ClockHandArrowProjectileEntity arrowEntity = new ClockHandArrowProjectileEntity(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });
    }
}
