package com.sosnitzka.taiga.traits;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import static com.sosnitzka.taiga.util.Utils.isNight;

public class TraitEmberFlame extends AbstractTrait {

    public TraitEmberFlame() {
        super("emberflame", TextFormatting.RED);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean
            wasCritical, boolean wasHit) {
        int dur = wasCritical ? 30 : 10;
        target.setFire(dur);
        target.addPotionEffect(new PotionEffect(MobEffects.WITHER, dur * 20, wasCritical ? 1 : 0));
    }
}
