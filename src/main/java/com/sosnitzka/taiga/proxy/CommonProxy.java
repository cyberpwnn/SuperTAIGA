package com.sosnitzka.taiga.proxy;

import com.sosnitzka.taiga.TAIGAConfiguration;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.materials.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sosnitzka.taiga.MaterialTraits.*;
import static slimeknights.tconstruct.library.utils.HarvestLevels.harvestLevelNames;

public class CommonProxy {
    public static List<Block> ores = new ArrayList<Block>();

    public void prepre() {

    }

    public void registerModels() {

    }

    public void setRenderInfo(Material material) {

    }

    public void registerFluidModels(Fluid fluid) {
    }

    public void initConfig() {
        TAIGAConfiguration.preInit();
    }

    public void registerServerCommands(FMLServerStartingEvent e) {
    }

    public void registerBookPages() {
    }

    public void regsiterKeyBindings() {

    }

    public void registerHarvestLevels() {
        harvestLevelNames.put(DURANITE, "harvestlevel.duranite");
        harvestLevelNames.put(VALYRIUM, "harvestlevel.valyrium");
        harvestLevelNames.put(VIBRANIUM, "harvestlevel.vibranium");
    }

    public void post() {
        ores = ForgeRegistries.BLOCKS.getEntries().stream()
                .filter((v) -> v.getValue().getLocalizedName().endsWith(" Ore"))
                .map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
