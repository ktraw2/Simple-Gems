package com.ktraw.simplegems.world;

import com.ktraw.simplegems.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import sun.reflect.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class OreGeneration {
    public static void setupOreGeneration() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            CountRangeConfig rubyOrePlacement = new CountRangeConfig(10, 8, 0, 30);
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.RUBY_ORE.getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(rubyOrePlacement)));

            try {
                CountRangeConfig amethystOrePlacement = new CountRangeConfig(12, 0, 0, 45);

                // Thanks Java for letting me do this
                Class<OreFeatureConfig.FillerBlockType> fillerBlockTypeClass = OreFeatureConfig.FillerBlockType.class;
                Constructor<?> constructor = fillerBlockTypeClass.getDeclaredConstructors()[0];
                constructor.setAccessible(true);

                Field constructorAccessorField = Constructor.class.getDeclaredField("constructorAccessor");
                constructorAccessorField.setAccessible(true);
                ConstructorAccessor ca = (ConstructorAccessor) constructorAccessorField.get(constructor);
                if (ca == null) {
                    Method acquireConstructorAccessorMethod = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");
                    acquireConstructorAccessorMethod.setAccessible(true);
                    ca = (ConstructorAccessor) acquireConstructorAccessorMethod.invoke(constructor);
                }

                Object[] oArg = new Object[4];
                oArg[0] = "END_STONE";
                oArg[1] = 4;
                oArg[2] = "end_stone";
                oArg[3] = new BlockMatcher(Blocks.END_STONE);
                OreFeatureConfig.FillerBlockType end_stone = (OreFeatureConfig.FillerBlockType) ca.newInstance(oArg);

                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(end_stone, ModBlocks.AMETHYST_ORE.getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(amethystOrePlacement)));
            } catch (Exception e) {
                System.err.println("Error in reflecting OreFeatureConfig.FillerBlockType, Amethyst Ore will not generate.");
                e.printStackTrace();
            }
        }
    }
}
