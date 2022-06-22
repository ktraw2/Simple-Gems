package com.ktraw.simplegems.world;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.registry.Blocks;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static net.minecraft.data.worldgen.features.OreFeatures.DEEPSLATE_ORE_REPLACEABLES;
import static net.minecraft.data.worldgen.features.OreFeatures.STONE_ORE_REPLACEABLES;

@Mod.EventBusSubscriber
public class OreGeneration {
    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, SimpleGems.MODID);
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, SimpleGems.MODID);

    public static final RegistryObject<ConfiguredFeature<?, ?>> ORE_RUBY_REPLACEMENTS = CONFIGURED_FEATURES.register(
            "ore_ruby",
            () -> new ConfiguredFeature<>(
                    Feature.ORE,
                    new OreConfiguration(
                            List.of(
                                    OreConfiguration.target(STONE_ORE_REPLACEABLES, Blocks.RUBY_ORE.get().defaultBlockState()),
                                    OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_RUBY_ORE.get().defaultBlockState())
                            ),
                            3
                    )
            )
    );
    public static final RegistryObject<PlacedFeature> ORE_RUBY_PLACEMENTS = PLACED_FEATURES.register(
            "ore_ruby",
            () -> new PlacedFeature(
                    ORE_RUBY_REPLACEMENTS.getHolder().get(),
                    commonOrePlacement(
                            10,
                            HeightRangePlacement.uniform(
                                    VerticalAnchor.absolute(-32),
                                    VerticalAnchor.absolute(32)
                            )
                    )
            )
    );

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }
//
//    @SubscribeEvent
//    public static void generateOres(BiomeLoadingEvent event) {
//        BiomeGenerationSettingsBuilder generation = event.getGeneration();
//        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_RUBY_PLACEMENTS.getHolder().get());
//    }

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
    }
}
