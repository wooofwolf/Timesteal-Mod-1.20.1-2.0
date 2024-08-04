package net.justwoofwolf.timestealmod.items.custom;

import net.justwoofwolf.timestealmod.entity.custom.ClockHandArrowProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ClockHandArrowItem extends ArrowItem {

    public ClockHandArrowItem(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new ClockHandArrowProjectileEntity(world, shooter);
    }
}
