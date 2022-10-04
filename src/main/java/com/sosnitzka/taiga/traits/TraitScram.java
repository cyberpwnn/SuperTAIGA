package com.sosnitzka.taiga.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import scala.tools.nsc.doc.model.Trait;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.tools.melee.item.FryPan;

public class TraitScram extends AbstractTrait {

    public static float chance = 1f;

    public TraitScram() {
        super("scram", TextFormatting.YELLOW);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean
            wasCritical, boolean wasHit) {
        if (random.nextFloat() <= chance) {
            player.world.playSound(null, player.getPosition(), Sounds.frypan_boing, SoundCategory.PLAYERS, 1.5f, 0.4f + (0.2f * TConstruct.random.nextFloat()));
            target.knockBack(target, random.nextFloat() * random.nextFloat() * 17, player.posX - target.posX, player
                    .posZ - target.posZ);
        }
    }
}



