package net.justwoofwolf.timestealmod.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.justwoofwolf.timestealmod.TimestealMod;
import net.justwoofwolf.timestealmod.entity.custom.ClockHandArrowProjectileEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClockHandArrowEntityRenderer extends ProjectileEntityRenderer<ClockHandArrowProjectileEntity> {
    public static final Identifier TEXTURE = new Identifier(TimestealMod.MOD_ID, "textures/entity/projectiles/clock_hand_arrow.png");

    public ClockHandArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ClockHandArrowProjectileEntity entity) {
        return TEXTURE;
    }
}
