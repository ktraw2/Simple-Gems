package com.ktraw.simplegems.blocks;

import com.ktraw.simplegems.blocks.generator.GeneratorBlockEntity;
import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserBlockEntity;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import lombok.Getter;
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
    public static SimpleGemsContainerBlock GENERATOR;

    @ObjectHolder("simplegems:generator")
    @Getter
    public static BlockEntityType<GeneratorBlockEntity> GENERATOR_TILE;

    @ObjectHolder("simplegems:generator")
    public static MenuType<GeneratorContainerMenu> GENERATOR_CONTAINER;

    @ObjectHolder("simplegems:infuser")
    public static SimpleGemsContainerBlock INFUSER;

    @ObjectHolder("simplegems:infuser")
    @Getter
    public static BlockEntityType<InfuserBlockEntity> INFUSER_TILE;

    @ObjectHolder("simplegems:infuser")
    public static MenuType<InfuserContainerMenu> INFUSER_CONTAINER;

    @ObjectHolder("simplegems:infuser")
    public static RecipeSerializer<InfuserRecipe> INFUSER_SERIALIZER;

    public static RecipeType<InfuserRecipe> INFUSER_RECIPE_TYPE;
}
