package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.blocks.generator.Generator;
import com.ktraw.simplegems.blocks.generator.GeneratorContainer;
import com.ktraw.simplegems.blocks.generator.GeneratorTile;
import com.ktraw.simplegems.blocks.infuser.Infuser;
import com.ktraw.simplegems.blocks.infuser.InfuserContainer;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import com.ktraw.simplegems.blocks.infuser.InfuserTile;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("simplegems:emerald_lamp")
    public static EmeraldLamp EMERALD_LAMP;

    @ObjectHolder("simplegems:inverted_emerald_lamp")
    public static EmeraldLamp INVERTED_EMERALD_LAMP;

    @ObjectHolder("simplegems:ruby_ore")
    public static RubyOre RUBY_ORE;

    @ObjectHolder("simplegems:deepslate_ruby_ore")
    public static RubyOre DEEPSLATE_RUBY_ORE;

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
    public static BlockEntityType<GeneratorTile> GENERATOR_TILE;

    @ObjectHolder("simplegems:generator")
    public static MenuType<GeneratorContainer> GENERATOR_CONTAINER;

    @ObjectHolder("simplegems:infuser")
    public static Infuser INFUSER;

    @ObjectHolder("simplegems:infuser")
    public static BlockEntityType<InfuserTile> INFUSER_TILE;

    @ObjectHolder("simplegems:infuser")
    public static MenuType<InfuserContainer> INFUSER_CONTAINER;

    @ObjectHolder("simplegems:infuser")
    public static RecipeSerializer<InfuserRecipe> INFUSER_SERIALIZER;

    public static RecipeType<InfuserRecipe> INFUSER_RECIPE_TYPE;
}
