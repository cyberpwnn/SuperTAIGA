package com.sosnitzka.taiga.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;

import java.util.List;

/**
 * Gives the tool bonus stats on crafting.
 * The bonus stats are distributed over time and are more or less random.
 * The stats that will be rewarded are already designated on the first time the tool is crafted
 */
public class TraitDecay extends TraitProgressiveStats {

    protected static int TICK_PER_STAT = 24;
    protected static int DURABILITY_STEP = 1;
    protected static float SPEED_STEP = 0.05f;
    protected static float ATTACK_STEP = 0.05f;

    public TraitDecay() {
        super("decay", TextFormatting.GREEN);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        // check if we have stats already distributed, and if not add them
        if (!hasPool(rootCompound)) {
            // ok, we need new stats. Let the fun begin!
            StatNBT data = new StatNBT();

            int statPoints = 800; // we distribute a whopping X points worth of stats!
            for (; statPoints > 0; statPoints--) {
                switch (random.nextInt(3)) {
                    // durability
                    case 0:
                        data.durability += DURABILITY_STEP;
                        break;
                    // speed
                    case 1:
                        data.speed += SPEED_STEP;
                        break;
                    // attack
                    case 2:
                        data.attack += ATTACK_STEP;
                        break;
                }
            }

            setPool(rootCompound, data);
        }

        super.applyEffect(rootCompound, modifierTag);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof FakePlayer || entity.world.isRemote || entity.ticksExisted % TICK_PER_STAT != 0 || playerIsBreakingBlock(entity)) {
            return;
        }

        NBTTagCompound root = TagUtil.getTagSafe(tool);
        StatNBT distributed = getBonus(root);
        ToolNBT data = TagUtil.getToolStats(tool);

        // attack
        if (entity.ticksExisted % (TICK_PER_STAT * 3) == 0) {
            float A = ATTACK_STEP * random.nextFloat();
            data.attack -= A;
            distributed.attack -= A;
        }
        // speed
        else if (entity.ticksExisted % (TICK_PER_STAT * 2) == 0) {
            float S = SPEED_STEP * random.nextFloat();
            data.speed -= S;
            distributed.speed -= S;
        }
        // durability
        else {
            int D = random.nextInt(DURABILITY_STEP) + 1;
            data.durability -= D;
            distributed.durability -= D;
        }

        // update tool stats
        TagUtil.setToolTag(root, data.get());
        // update statistics on distributed stats
        setBonus(root, distributed);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        StatNBT pool = getBonus(TagUtil.getTagSafe(tool));

        return ImmutableList.of(HeadMaterialStats.formatDurability(pool.durability),
                HeadMaterialStats.formatMiningSpeed(pool.speed),
                HeadMaterialStats.formatAttack(pool.attack));
    }
}