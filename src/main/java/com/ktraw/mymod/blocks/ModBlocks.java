package com.ktraw.mymod.blocks;

import com.ktraw.mymod.blocks.generator.Generator;
import com.ktraw.mymod.blocks.generator.GeneratorContainer;
import com.ktraw.mymod.blocks.generator.GeneratorTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("mymod:emerald_lamp")
    public static EmeraldLamp EMERALD_LAMP;

    @ObjectHolder("mymod:ruby_ore")
    public static RubyOre RUBY_ORE;

    @ObjectHolder("mymod:ruby_block")
    public static RubyBlock RUBY_BLOCK;

    @ObjectHolder("mymod:amethyst_ore")
    public static AmethystOre AMETHYST_ORE;

    @ObjectHolder("mymod:amethyst_block")
    public static AmethystBlock AMETHYST_BLOCK;

    @ObjectHolder("mymod:generator")
    public static Generator GENERATOR;

    @ObjectHolder("mymod:generator")
    public static TileEntityType<GeneratorTile> GENERATOR_TILE;

    @ObjectHolder("mymod:generator")
    public static ContainerType<GeneratorContainer> GENERATOR_CONTAINER;
}
