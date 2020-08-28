package com.ktraw.simplegems;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ktraw.simplegems.blocks.*;
import com.ktraw.simplegems.blocks.generator.*;
import com.ktraw.simplegems.blocks.infuser.*;
import com.ktraw.simplegems.events.PlayerEvents;
import com.ktraw.simplegems.items.*;
import com.ktraw.simplegems.items.armor.*;
import com.ktraw.simplegems.items.rings.*;
import com.ktraw.simplegems.items.tools.*;
import com.ktraw.simplegems.setup.ClientProxy;
import com.ktraw.simplegems.setup.IProxy;
import com.ktraw.simplegems.setup.ModSetup;
import com.ktraw.simplegems.setup.ServerProxy;
import com.ktraw.simplegems.tools.*;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.potion.HealthBoostEffect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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

import java.util.Arrays;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("simplegems")
public class SimpleGems
{
    public static final String MODID = "simplegems";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ModSetup setup = ModSetup.getSetup();

    private static final int POTION_TICKS = 20;

    private static final Multimap<Attribute, AttributeModifier> heavyRingAttributes = HashMultimap.create();
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("5D6F0BAB3-1086-46AC-F896-C61C5CEE99CC");
    static {
        heavyRingAttributes.put(Attributes.MAX_HEALTH, new AttributeModifier(MAX_HEALTH_UUID, "Ring modifier", 10.0, AttributeModifier.Operation.ADDITION));
    }

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleGems() {
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
            return Direction.getFacingFromVector((float) (entity.serverPosX - clickedBlock.getX()), (float) (entity.serverPosY - clickedBlock.getY()), (float) (entity.serverPosZ - clickedBlock.getZ()));
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
            registry.register(new GemBlock());
            registry.register(new Generator());
            registry.register(new Infuser());
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
            registry.register(new BlockItem(ModBlocks.GEM_BLOCK, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("gem_block"));
            registry.register(new BlockItem(ModBlocks.GENERATOR, new Item.Properties().group(setup.getCreativeTab()).rarity(Rarity.RARE)).setRegistryName("generator"));
            registry.register(new BlockItem(ModBlocks.INFUSER, new Item.Properties().group(setup.getCreativeTab())).setRegistryName("infuser"));

            // Regular items
            registry.register(new Ruby());
            registry.register(new Amethyst());
            registry.register(new Gem());
            registry.register(new ChargedEmeraldDust());
            registry.register(new EnderApple());
            registry.register(new GoldRing());
            registry.register(new GemRing("gem_ring", null));
            registry.register(new GemRing("ring_of_haste", new SingleEffectProvider(new EffectInstanceWrapper(Effects.HASTE, POTION_TICKS, 1))));
            registry.register(new GemRing("ring_of_levitation", new SingleEffectProvider(new EffectInstanceWrapper(Effects.LEVITATION, POTION_TICKS))));
            registry.register(new GemRing("ring_of_vulnerable_strength", new MultiEffectProvider(Arrays.asList(new EffectInstanceWrapper(Effects.STRENGTH, POTION_TICKS, 1), new EffectInstanceWrapper(Effects.RESISTANCE, POTION_TICKS, -5)))));
            registry.register(new GemRing("ring_of_heavy", new SingleEffectProvider(new EffectInstanceWrapper(Effects.SLOWNESS, POTION_TICKS, 3)), heavyRingAttributes, new TranslationTextComponent("tooltip.simplegems.heavy").func_230530_a_( Style.EMPTY.setColor(Color.func_240744_a_(TextFormatting.RED)))));
            registry.register(new GemPickaxe());
            registry.register(new GemSword());
            registry.register(new GemAxe());
            registry.register(new GemShovel());
            registry.register(new GemHoe());
            registry.register(new GemHelmet());
            registry.register(new GemChestplate());
            registry.register(new GemLeggings());
            registry.register(new GemBoots());

        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

            registry.register(TileEntityType.Builder.create(GeneratorTile::new, ModBlocks.GENERATOR).build(null).setRegistryName("generator"));
            registry.register(TileEntityType.Builder.create(InfuserTile::new, ModBlocks.INFUSER).build(null).setRegistryName("infuser"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

            registry.register(IForgeContainerType.create((windowId, inv, data) -> new GeneratorContainer(windowId, inv)).setRegistryName("generator"));

            registry.register(IForgeContainerType.create(((windowId, inv, data) -> new InfuserContainer(windowId, inv))).setRegistryName("infuser"));
        }

        @SubscribeEvent
        public static void onRecipeSerializerRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

            /* doing this first line here because Forge doesn't have its own registry for recipe types (yet?)
               TODO come back to this line of code if Forge has it's own registry for recipe types */
            ModBlocks.INFUSER_RECIPE_TYPE = IRecipeType.register(SimpleGems.MODID + ":infuser");
            registry.register(new InfuserRecipe.Serializer());
        }
    }
}
