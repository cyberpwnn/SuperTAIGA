package com.sosnitzka.taiga.util;

import com.google.common.collect.Lists;
import com.sosnitzka.taiga.world.MeteorWorldSaveData;
import com.sosnitzka.taiga.world.WorldGenMinable;
import net.minecraft.block.BlockStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.sosnitzka.taiga.util.Utils.nextInt;

public class Generator {

    public static void generateOre(IBlockState newState, IBlockState oldState, Random random, int chunkX, int chunkZ,
                                   World world, int count, int minY, int maxY, int minSize, int maxSize) {
        generateOre(newState, oldState, null, null, random, chunkX, chunkZ, world, count, 100, minY, maxY, minSize,
                maxSize, null);
    }

    public static void generateOre(IBlockState newState, IBlockState oldState, Random random, int chunkX, int chunkZ,
                                   World world, int count, int chance, int minY, int maxY, int minSize, int maxSize,
                                   List<Biome> biome) {
        generateOre(newState, oldState, null, null, random, chunkX, chunkZ, world, count, chance, minY, maxY,
                minSize, maxSize, biome);
    }

    public static void generateOre(IBlockState newState, IBlockState oldState, IProperty property, Comparable
            comparable, Random random, int chunkX, int chunkZ, World world, int count, int chance, int minY, int
                                           maxY, int minSize, int maxSize, List<Biome> biome) {
        int size = minSize + random.nextInt(maxSize - minSize);
        int height = maxY - minY;
        for (int i = 0; i < count; i++) {
            if (0.01f * chance >= random.nextFloat()) {
                int posX = chunkX + random.nextInt(16);
                int posY = random.nextInt(height) + minY;
                int posZ = chunkZ + random.nextInt(16);
                BlockPos cPos = new BlockPos(posX, posY, posZ);
                if (biome == null || biome.contains(world.getBiome(cPos))) {
                    new WorldGenMinable(newState, size, StateMatcher.forState(oldState, property, comparable))
                            .generate(world, random, new BlockPos(posX, posY, posZ));
                }
            }
        }
    }

    public static void generateOre(List<IBlockState> replaceBlockList, IBlockState replacementBlock, Random random,
                                   int chunkX, int chunkZ, World world, int count, int minY, int maxY, int chance) {
        if (random.nextFloat() < (float) (0.01 * chance))
            generateOreDescending(replaceBlockList, replacementBlock, random, chunkX, chunkZ, world, count, minY, maxY);
    }

    public static void generateOreDescending(List<IBlockState> replaceBlockList, IBlockState replacementBlock, Random
            random, int chunkX, int chunkZ, World world, int count, int minY, int maxY) {
        for (int i = 0; i < count; i++) {
            int posX = chunkX + random.nextInt(16);
            int posZ = chunkZ + random.nextInt(16);
            BlockPos cPos = new BlockPos(posX, maxY, posZ);
            if (replaceBlockList.contains(world.getBlockState(cPos)) && replaceBlockList.contains(world.getBlockState
                    (cPos.up()))) {
                continue;
            }
            if (replaceBlockList.contains(world.getBlockState(cPos)) && !replaceBlockList.contains(world
                    .getBlockState(cPos.up())))
                world.setBlockState(cPos, replacementBlock);
            while (!replaceBlockList.contains(world.getBlockState(cPos.down())) && cPos.getY() > minY) {
                cPos = cPos.down();
            }
            if (replaceBlockList.contains(world.getBlockState(cPos.down())))
                world.setBlockState(cPos.down(), replacementBlock);
        }
    }

    public static void generateOreStoneVariant(IBlockState newState, BlockStone.EnumType type, Random random, int
            chunkX, int chunkZ, World world, int count) {
        List<BlockStone.EnumType> list = newArrayList(type);
        for (int i = 0; i < count; i += 2) {
            int posX = chunkX + random.nextInt(16);
            int posZ = chunkZ + random.nextInt(16);
            BlockPos cPos = new BlockPos(posX, random.nextInt(64) + 32, posZ);
            IBlockState state = world.getBlockState(cPos);
            if (state.getBlock().equals(Blocks.STONE.getDefaultState().getBlock())) {
                if (list.contains(state.getValue(BlockStone.VARIANT))) {
                    world.setBlockState(cPos, newState);
                }
            } else {
                while (cPos.getY() >= 0) {
                    cPos = cPos.down();
                    state = world.getBlockState(cPos);
                    if (state.getBlock().equals(Blocks.STONE.getDefaultState().getBlock())) {
                        if (list.contains(state.getValue(BlockStone.VARIANT))) {
                            world.setBlockState(cPos, newState);
                            break;
                        }
                    }
                }
            }
            cPos = new BlockPos(posX, random.nextInt(32), posZ);
            state = world.getBlockState(cPos);
            if (state.getBlock().equals(Blocks.STONE.getDefaultState().getBlock())) {
                if (list.contains(state.getValue(BlockStone.VARIANT))) {
                    world.setBlockState(cPos, newState);
                }
            } else {
                while (cPos.getY() <= 96) {
                    cPos = cPos.up();
                    state = world.getBlockState(cPos);
                    if (state.getBlock().equals(Blocks.STONE.getDefaultState().getBlock())) {
                        if (list.contains(state.getValue(BlockStone.VARIANT))) {
                            world.setBlockState(cPos, newState);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void generateOreBottom(IBlockState oldState, IBlockState newState, Random random, int chunkX, int
            chunkZ, World world, int chance, int spread, int maxY) {
        for (int i = 0; i < chance; i++) {
            int posX = chunkX + random.nextInt(16);
            int posY = 0;
            int posZ = chunkZ + random.nextInt(16);
            BlockPos cPos = new BlockPos(posX, posY, posZ);
            if (Blocks.AIR.getDefaultState().equals(world.getBlockState(cPos))) {
                while (world.getBlockState(cPos).equals(Blocks.AIR.getDefaultState()) && cPos.getY() < maxY) {
                    cPos = cPos.up();
                }
                if (world.getBlockState(cPos).equals(oldState)) {
                    world.setBlockState(cPos.up(random.nextInt(spread)), newState);
                }
            }
        }
    }

    public static void generateCube(boolean fly, IBlockState centerBlock, IBlockState hullBlock, Random random, int
            chunkX, int chunkZ, World world, int count, int chance, int minY, int maxY, int maxS) {
        for (int i = 0; i < count; i++) {
            if (random.nextFloat() < 0.01 * chance) {
                int outer = nextInt(random, 1, maxS);
                int inner = random.nextInt(2);
                int posX = chunkX + random.nextInt(16);
                int posY = nextInt(random, minY, maxY);
                int posZ = chunkZ + random.nextInt(16);
                BlockPos cPos = new BlockPos(posX, posY, posZ);
                if (!fly) {
                    if (world.getBlockState(cPos).equals(Blocks.AIR.getDefaultState()) && world.getBlockState(cPos
                            .down()).equals(Blocks.AIR.getDefaultState())) {
                        // we are in mid air, go down
                        while (world.getBlockState(cPos.down()).equals(Blocks.AIR.getDefaultState())) {
                            cPos = cPos.down();
                        }
                    }
                }
                cPos.down((random.nextInt(4) + 2) * outer);
                for (int x = -inner; x <= inner; x++) {
                    for (int y = -inner; y <= inner; y++) {
                        for (int z = -inner; z <= inner; z++) {
                            if (!world.getBlockState(cPos).equals(Blocks.AIR.getDefaultState()))
                                continue;
                            world.setBlockState(new BlockPos(cPos.getX() + x, cPos.getY() + y, cPos.getZ() + z),
                                    centerBlock);
                        }
                    }
                }
                for (int x = -outer; x <= outer; x++) {
                    for (int y = -outer; y <= outer; y++) {
                        for (int z = -outer; z <= outer; z++) {
                            BlockPos nPos = new BlockPos(cPos.getX() + x, cPos.getY() + y, cPos.getZ() + z);
                            if (world.getBlockState(nPos).equals(centerBlock) || !world.getBlockState(nPos).equals
                                    (Blocks.AIR.getDefaultState()))
                                continue;
                            world.setBlockState(nPos, hullBlock);
                        }
                    }
                }

            }
        }
    }

    public static int generateMeteor(IBlockState centerBlock, IBlockState hullBlock, Random random, int chunkX, int
            chunkZ, World world, int count, int chance, int minY, int maxY) {
        Set<Item> validSurface = new HashSet<Item>();
        List<String> oredictentries = Lists.newArrayList("dirt", "grass", "stone", "sand", "gravel", "cobblestone",
                "sandstone");
        for (String e : oredictentries) {
            for (ItemStack stack : OreDictionary.getOres(e)) {
                validSurface.add(stack.getItem());
            }
        }

        int mGenerated = 0;

        for (int i = 0; i < count; i++) {
            if (random.nextFloat() < 0.01 * chance) {
                int r = nextInt(random, 1, 5);
                int posX = chunkX + random.nextInt(16);
                int posY = nextInt(random, minY, maxY);
                int posZ = chunkZ + random.nextInt(16);
                BlockPos cPos = new BlockPos(posX, posY, posZ);
                if (world.getBlockState(cPos).equals(Blocks.AIR.getDefaultState()) && world.getBlockState(cPos.down()
                ).equals(Blocks.AIR.getDefaultState())) {
                    // we are in mid air, go down
                    while (world.getBlockState(cPos.down()).equals(Blocks.AIR.getDefaultState())) {
                        cPos = cPos.down();

                        if (cPos.getY() < minY)
                            break;
                    }
                }
                if (!validSurface.contains(Item.getItemFromBlock(world.getBlockState(cPos.down()).getBlock())))
                    continue;
                cPos = cPos.down(random.nextInt(r * 2) + r + 1);

                MeteorWorldSaveData saveData = MeteorWorldSaveData.getForWorld(world);
                saveData.addPos(cPos);
                saveData.markDirty();

                mGenerated++;

                int t = 1;
                if (r > 3) t = random.nextInt(r - 1);
                for (int x = -t; x <= t; x++) {
                    for (int y = -t; y <= t; y++) {
                        for (int z = -t; z <= t; z++) {
                            if (MathHelper.sqrt(x * x + y * y + z * z) > t) {
                                continue;
                            }
                            world.setBlockState(new BlockPos(cPos.getX() + x, cPos.getY() + y, cPos.getZ() + z),
                                    centerBlock);
                        }
                    }
                }
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        for (int z = -r; z <= r; z++) {
                            if (MathHelper.sqrt(x * x + y * y + z * z) > r) {
                                continue;
                            }
                            BlockPos nPos = new BlockPos(cPos.getX() + x, cPos.getY() + y, cPos.getZ() + z);
                            if (world.getBlockState(nPos).equals(centerBlock))
                                continue;
                            world.setBlockState(nPos, hullBlock);
                        }
                    }
                }
            }
        }

        return mGenerated;
    }
}
