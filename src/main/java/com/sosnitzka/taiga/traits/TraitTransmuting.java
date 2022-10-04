package com.sosnitzka.taiga.traits;

import com.sosnitzka.taiga.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;


public class TraitTransmuting extends AbstractTrait {
    public TraitTransmuting() {
        super("transmuting", TextFormatting.DARK_PURPLE);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase
            player, boolean wasEffective) {

        if(!world.isRemote && wasEffective && random.nextFloat() < 0.025)
        {
            EntityItem ex = new EntityItem(world);
            ex.setItem(new ItemStack(Item.getItemFromBlock(CommonProxy.ores.get(random.nextInt(CommonProxy.ores.size())))));
            ex.setPosition(pos.getX()+0.5, pos.getY()+0.5, pos.getZ() + 0.5);
            ex.setGlowing(true);
            world.spawnEntity(ex);
        }
    }
}



