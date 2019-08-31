package com.ktraw.mymod;

import com.ktraw.mymod.blocks.*;
import com.ktraw.mymod.blocks.generator.Generator;
import com.ktraw.mymod.blocks.generator.GeneratorContainer;
import com.ktraw.mymod.blocks.generator.GeneratorTile;
import com.ktraw.mymod.events.PlayerEvents;
import com.ktraw.mymod.items.*;
import com.ktraw.mymod.items.armor.GemBoots;
import com.ktraw.mymod.items.armor.GemChestplate;
import com.ktraw.mymod.items.armor.GemHelmet;
import com.ktraw.mymod.items.armor.GemLeggings;
import com.ktraw.mymod.items.tools.*;
import com.ktraw.mymod.setup.ClientProxy;
import com.ktraw.mymod.setup.IProxy;
import com.ktraw.mymod.setup.ModSetup;
import com.ktraw.mymod.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mymod")
public class MyMod
{
    public static final String MODID = "mymod";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ModSetup setup = ModSetup.getSetup();

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public MyMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity, boolean horizontalOnly) {
        if (horizontalOnly) {
            return entity.getHorizontalFacing().getOpposite();
        }
        else {
            return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            IForgeRegistry<Block> registry = event.getRegistry();

            registry.register(new EmeraldLamp());
            registry.register(new RubyOre());
            registry.register(new RubyBlock());
            registry.register(new AmethystOre());
            registry.register(new AmethystBlock());
            registry.register(new Generator());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();

            // Blocks as items
            registry.register(new BlockItem(ModBlocks.EMERALD_LAMP, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("emerald_lamp"));
            registry.register(new BlockItem(ModBlocks.RUBY_ORE, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("ruby_ore"));
            registry.register(new BlockItem(ModBlocks.RUBY_BLOCK, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("ruby_block"));
            registry.register(new BlockItem(ModBlocks.AMETHYST_ORE, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("amethyst_ore"));
            registry.register(new BlockItem(ModBlocks.AMETHYST_BLOCK, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("amethyst_block"));
            registry.register(new BlockItem(ModBlocks.GENERATOR, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("generator"));

            // Regular items
            registry.register(new Ruby());
            registry.register(new ChargedEmeraldDust());
            registry.register(new Gem());
            registry.register(new GemPickaxe());
            registry.register(new GemSword());
            registry.register(new GemAxe());
            registry.register(new GemShovel());
            registry.register(new GemHoe());
            registry.register(new GemHelmet());
            registry.register(new GemChestplate());
            registry.register(new GemLeggings());
            registry.register(new GemBoots());
            registry.register(new Amethyst());
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

            registry.register(TileEntityType.Builder.create(GeneratorTile::new, ModBlocks.GENERATOR).build(null).setRegistryName("generator"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

            registry.register(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();

                return new GeneratorContainer(windowId, MyMod.proxy.getClientWorld(), pos, inv, MyMod.proxy.getClientPlayer());
            }).setRegistryName("generator"));
        }
    }
}