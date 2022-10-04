package com.sosnitzka.taiga.traits;

import com.sosnitzka.taiga.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;

import static com.sosnitzka.taiga.Keybindings.altKey;


public class TraitLifeSip extends TraitProgressiveStats {

    protected static int TICK_PER_STAT = 8;

    public TraitLifeSip() {
        super(TraitLifeSip.class.getSimpleName().toLowerCase().substring(5), TextFormatting.RED);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote) {
            NBTTagCompound tag = TagUtil.getExtraTag(tool);
            EntityLivingBase player = (EntityLivingBase) entity;
            Utils.GeneralNBTData data = Utils.GeneralNBTData.read(tag);
            NBTTagCompound root = TagUtil.getTagSafe(tool);
            StatNBT distributed = getBonus(root);
            if (data.active) {
                if (!TagUtil.hasEnchantEffect(root))
                    TagUtil.setEnchantEffect(root, true);

                if (entity instanceof FakePlayer || entity.ticksExisted % TICK_PER_STAT != 0) {
                    return;
                }

                ToolNBT stat = TagUtil.getToolStats(tool);
                if(player.getHealth() > 0 && !player.isDead && tool.isItemDamaged() && tool.getItemDamage() >=5)
                {
                    player.setHealth(player.getHealth() - 1);
                    ToolHelper.healTool(tool, random.nextInt(10)+5, player);
                }
                TagUtil.setToolTag(root, stat.get());
            } else {
                if (TagUtil.hasEnchantEffect(root))
                    TagUtil.setEnchantEffect(root, false);
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        World w = event.getWorld();
        ItemStack tool = event.getEntityPlayer().getHeldItemMainhand();
        if (!w.isRemote && TinkerUtil.hasTrait(TagUtil.getTagSafe(tool), identifier) && altKey.isKeyDown()) {
            NBTTagCompound tag = TagUtil.getExtraTag(tool);
            Utils.GeneralNBTData data = Utils.GeneralNBTData.read(tag);
            NBTTagCompound root = TagUtil.getTagSafe(tool);
            StatNBT distributed = getBonus(root);
            ToolNBT stat = TagUtil.getToolStats(tool);
            if (data.active) {
                data.active = false;
                TagUtil.setEnchantEffect(root, false);
                TagUtil.setExtraTag(root, tag);
                data.write(tag);
            } else {
                TagUtil.setToolTag(root, stat.get());
                setBonus(root, distributed);
                data.active = true;
                data.write(tag);

                TagUtil.setExtraTag(root, tag);
                data.write(tag);
            }
        }
    }
}
