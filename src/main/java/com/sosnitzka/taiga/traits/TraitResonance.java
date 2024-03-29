package com.sosnitzka.taiga.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitResonance extends AbstractTrait {

    public static float chance = 0.5f;

    public TraitResonance() {
        super("resonance", TextFormatting.AQUA);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean
            wasCritical, boolean wasHit) {
        if (random.nextFloat() <= chance) {
            player.world.playSound(null, player.getPosition(), Sounds.frypan_boing, SoundCategory.PLAYERS, 1.5f, 0.4f + (0.2f * TConstruct.random.nextFloat()));
            target.knockBack(target, random.nextFloat() * random.nextFloat() * 10, player.posX - target.posX, player
                    .posZ - target.posZ);
        }
    }
}



