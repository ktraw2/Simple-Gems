package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.BaseGemBlock;
import com.ktraw.simplegems.blocks.EmeraldLamp;
import com.ktraw.simplegems.blocks.RubyOre;
import com.ktraw.simplegems.blocks.SimpleGemsContainerBlock;
import com.ktraw.simplegems.blocks.generator.GeneratorBlockEntity;
import com.ktraw.simplegems.blocks.infuser.InfuserBlockEntity;
import com.ktraw.simplegems.util.Material;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Blocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleGems.MODID);

    public static final RegistryObject<EmeraldLamp> EMERALD_LAMP = BLOCKS.register(EmeraldLamp.getRegistryName(false), () -> new EmeraldLamp(false));
    public static final RegistryObject<EmeraldLamp> INVERTED_EMERALD_LAMP = BLOCKS.register(EmeraldLamp.getRegistryName(true), () -> new EmeraldLamp(true));
    public static final RegistryObject<RubyOre> RUBY_ORE = BLOCKS.register(RubyOre.getRegistryName(false), () -> new RubyOre(false));
    public static final RegistryObject<RubyOre> DEEPSLATE_RUBY_ORE = BLOCKS.register(RubyOre.getRegistryName(true), () -> new RubyOre(true));
    public static final RegistryObject<BaseGemBlock> RUBY_BLOCK = BLOCKS.register("ruby_block", BaseGemBlock::new);
    public static final RegistryObject<BaseGemBlock> AMETHYST_BLOCK = BLOCKS.register("amethyst_block", BaseGemBlock::new);
    public static final RegistryObject<BaseGemBlock> GEM_BLOCK = BLOCKS.register("gem_block", () -> new BaseGemBlock(Material.METAL, 7f, 45f));
    public static final RegistryObject<SimpleGemsContainerBlock> GENERATOR = BLOCKS.register("generator", () -> new SimpleGemsContainerBlock(BlockEntities::getGENERATOR, GeneratorBlockEntity::tick, GeneratorBlockEntity::new));
    public static final RegistryObject<SimpleGemsContainerBlock> INFUSER = BLOCKS.register("infuser", () -> new SimpleGemsContainerBlock(BlockEntities::getINFUSER, InfuserBlockEntity::tick, InfuserBlockEntity::new));

    public static void register(final IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
