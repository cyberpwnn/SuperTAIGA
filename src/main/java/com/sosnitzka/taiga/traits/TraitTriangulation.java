package com.sosnitzka.taiga.traits;

import com.sosnitzka.taiga.TAIGA;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCrit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.client.particle.Particles;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitTriangulation extends AbstractTrait {

    public static float chance = 0.5f;

    public TraitTriangulation() {
        super("triangulation", TextFormatting.RED);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean wasCritical, boolean wasHit) {
        target.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, target.getEntityBoundingBox().grow(32)).forEach(entity -> {
            if(entity.getTags().contains("triangulation")) {
                entity.attackEntityFrom(DamageSource.MAGIC, damage * 0.25f);
                double x = target.posX - player.posX;
                double y = 0.65;
                double z = target.posZ - player.posZ;
                double m = Math.sqrt(x * x + y * y + z * z);

                if(Math.random() < 0.125) {
                    target.getTags().remove("triangulation");
                }

                entity.addVelocity(x / m, y / m, z / m);
            }
        });

        if(!target.getTags().contains("triangulation")) {
            target.addTag("triangulation");
        }
    }
}



