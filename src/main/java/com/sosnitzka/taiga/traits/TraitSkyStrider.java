package com.sosnitzka.taiga.traits;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class TraitSkyStrider extends AbstractTrait {

    public TraitSkyStrider() {
        super("skystrider", TextFormatting.DARK_GRAY);
    }

    @Override
    public void onUpdate(ItemStack item, World world, Entity entity, int i, boolean b) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer e = (EntityPlayer) entity;
            if (TinkerUtil.hasTrait(TagUtil.getTagSafe(e.getHeldItemMainhand()), identifier)) {
                e.addPotionEffect(new PotionEffect(MobEffects.HASTE, 100, 1));
                e.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 1));
            }
        }
    }
}
