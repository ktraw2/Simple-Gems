package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.blocks.generator.Generator;
import com.ktraw.simplegems.blocks.generator.GeneratorContainer;
import com.ktraw.simplegems.blocks.generator.GeneratorTile;
import com.ktraw.simplegems.blocks.infuser.Infuser;
import com.ktraw.simplegems.blocks.infuser.InfuserContainer;
import com.ktraw.simplegems.blocks.infuser.InfuserTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("simplegems:emerald_lamp")
    public static EmeraldLamp EMERALD_LAMP;

    @ObjectHolder("simplegems:ruby_ore")
    public static RubyOre RUBY_ORE;

    @ObjectHolder("simplegems:ruby_block")
    public static RubyBlock RUBY_BLOCK;

    @ObjectHolder("simplegems:amethyst_ore")
    public static AmethystOre AMETHYST_ORE;

    @ObjectHolder("simplegems:amethyst_block")
    public static AmethystBlock AMETHYST_BLOCK;

    @ObjectHolder("simplegems:gem_block")
    public static GemBlock GEM_BLOCK;

    @ObjectHolder("simplegems:generator")
    public static Generator GENERATOR;

    @ObjectHolder("simplegems:generator")
    public static TileEntityType<GeneratorTile> GENERATOR_TILE;

    @ObjectHolder("simplegems:generator")
    public static ContainerType<GeneratorContainer> GENERATOR_CONTAINER;

    @ObjectHolder("simplegems:infuser")
    public static Infuser INFUSER;

    @ObjectHolder("simplegems:infuser")
    public static TileEntityType<InfuserTile> INFUSER_TILE;

    @ObjectHolder("simplegems:infuser")
    public static ContainerType<InfuserContainer> INFUSER_CONTAINER;
}
