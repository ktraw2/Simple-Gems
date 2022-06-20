package com.ktraw.simplegems;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ktraw.simplegems.blocks.BaseGemBlock;
import com.ktraw.simplegems.blocks.EmeraldLamp;
import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.blocks.RubyOre;
import com.ktraw.simplegems.blocks.SimpleGemsContainerBlock;
import com.ktraw.simplegems.blocks.generator.GeneratorBlockEntity;
import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserBlockEntity;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import com.ktraw.simplegems.events.PlayerEvents;
import com.ktraw.simplegems.items.BaseItem;
import com.ktraw.simplegems.items.ChargedEmeraldDust;
import com.ktraw.simplegems.items.PurpleApple;
import com.ktraw.simplegems.items.armor.BaseGemArmorItem;
import com.ktraw.simplegems.items.armor.GemArmorMaterials;
import com.ktraw.simplegems.items.rings.GemRing;
import com.ktraw.simplegems.items.tools.BaseGemAxe;
import com.ktraw.simplegems.items.tools.BaseGemHoe;
import com.ktraw.simplegems.items.tools.GemItemTiers;
import com.ktraw.simplegems.items.tools.BaseGemPickaxe;
import com.ktraw.simplegems.items.tools.BaseGemShovel;
import com.ktraw.simplegems.items.tools.BaseGemSword;
import com.ktraw.simplegems.setup.ClientProxy;
import com.ktraw.simplegems.setup.IProxy;
import com.ktraw.simplegems.setup.ModSetup;
import com.ktraw.simplegems.setup.ServerProxy;
import com.ktraw.simplegems.util.mobeffects.MobEffectInstanceWrapper;
import com.ktraw.simplegems.util.mobeffects.MultiMobEffectProvider;
import com.ktraw.simplegems.util.mobeffects.SingleMobEffectProvider;
import com.ktraw.simplegems.world.OreGeneration;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public static ModSetup setup = ModSetup.getSetup();

    private static final int POTION_TICKS = 20;

    private static final Multimap<Attribute, AttributeModifier> heavyRingAttributes = HashMultimap.create();
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("5aabe6ee-9d50-408d-a46d-e74375bbf931");
    static {
        heavyRingAttributes.put(Attributes.MAX_HEALTH, new AttributeModifier(MAX_HEALTH_UUID, "Ring modifier", 10.0, AttributeModifier.Operation.ADDITION));
    }

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleGems() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());

        OreGeneration.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity, boolean horizontalOnly) {
        if (horizontalOnly) {
            return entity.getDirection()/*.getHorizontalFacing()*/.getOpposite();
        }
        else {
            return Direction.getNearest((float) (entity.getX() - clickedBlock.getX()), (float) (entity.getY() - clickedBlock.getY()), (float) (entity.getZ() - clickedBlock.getZ()));
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            IForgeRegistry<Block> registry = event.getRegistry();

            registry.register(new EmeraldLamp(false));
            registry.register(new EmeraldLamp(true));
            registry.register(new RubyOre(false));
            registry.register(new RubyOre(true));
            registry.register(new BaseGemBlock("ruby_block"));
            registry.register(new BaseGemBlock("amethyst_block"));
            registry.register(new BaseGemBlock("gem_block", Material.METAL, 7f, 45f));
            registry.register(new SimpleGemsContainerBlock("generator", ModBlocks::getGENERATOR_TILE, GeneratorBlockEntity::tick, GeneratorBlockEntity::new));
            registry.register(new SimpleGemsContainerBlock("infuser", ModBlocks::getINFUSER_TILE, InfuserBlockEntity::tick, InfuserBlockEntity::new));
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();

            /* Blocks as items */
            registry.register(new BlockItem(ModBlocks.EMERALD_LAMP, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("emerald_lamp"));
            registry.register(new BlockItem(ModBlocks.INVERTED_EMERALD_LAMP, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("inverted_emerald_lamp"));
            registry.register(new BlockItem(ModBlocks.RUBY_ORE, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("ruby_ore"));
            registry.register(new BlockItem(ModBlocks.DEEPSLATE_RUBY_ORE, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("deepslate_ruby_ore"));
            registry.register(new BlockItem(ModBlocks.RUBY_BLOCK, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("ruby_block"));
            registry.register(new BlockItem(ModBlocks.AMETHYST_BLOCK, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("amethyst_block"));
            registry.register(new BlockItem(ModBlocks.GEM_BLOCK, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("gem_block"));
            registry.register(new BlockItem(ModBlocks.GENERATOR, new Item.Properties().tab(setup.getCreativeTab()).rarity(Rarity.RARE)).setRegistryName("generator"));
            registry.register(new BlockItem(ModBlocks.INFUSER, new Item.Properties().tab(setup.getCreativeTab())).setRegistryName("infuser"));

            /* Regular items */
            registry.register(new BaseItem("ruby"));
            registry.register(new BaseItem("amethyst"));
            registry.register(new BaseItem("gem", Rarity.RARE));
            registry.register(new ChargedEmeraldDust());
            registry.register(new PurpleApple());
            // Rings
            registry.register(new BaseItem("gold_ring", 1));
            registry.register(new GemRing("gem_ring", null));
            registry.register(new GemRing("ring_of_haste", new SingleMobEffectProvider(new MobEffectInstanceWrapper(MobEffects.DIG_SPEED, POTION_TICKS, 1))));
            registry.register(new GemRing("ring_of_levitation", new SingleMobEffectProvider(new MobEffectInstanceWrapper(MobEffects.LEVITATION, POTION_TICKS))));
            registry.register(new GemRing("ring_of_vulnerable_strength", new MultiMobEffectProvider(Arrays.asList(new MobEffectInstanceWrapper(MobEffects.DAMAGE_BOOST, POTION_TICKS, 1), new MobEffectInstanceWrapper(MobEffects.DAMAGE_RESISTANCE, POTION_TICKS, -5)))));
            registry.register(new GemRing("ring_of_heavy", new SingleMobEffectProvider(new MobEffectInstanceWrapper(MobEffects.MOVEMENT_SLOWDOWN, POTION_TICKS, 3)), heavyRingAttributes, new TranslatableComponent("tooltip.simplegems.heavy").setStyle( Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)))));
            // Tools/Weapons
            // Gem
            registry.register(new BaseGemPickaxe(GemItemTiers.getGemItemTier(), Rarity.RARE));
            registry.register(new BaseGemSword(GemItemTiers.getGemItemTier(), Rarity.RARE));
            registry.register(new BaseGemAxe(GemItemTiers.getGemItemTier(), Rarity.RARE));
            registry.register(new BaseGemShovel(GemItemTiers.getGemItemTier(), Rarity.RARE));
            registry.register(new BaseGemHoe(GemItemTiers.getGemItemTier(), Rarity.RARE));
            // Nethergem
            registry.register(new BaseGemPickaxe(GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether"));
            registry.register(new BaseGemSword(GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether"));
            registry.register(new BaseGemAxe(GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether"));
            registry.register(new BaseGemShovel(GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether"));
            registry.register(new BaseGemHoe(GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether"));
            // Armor
            // Gem
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.HEAD, Rarity.RARE));
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.CHEST, Rarity.RARE));
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.LEGS, Rarity.RARE));
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.FEET, Rarity.RARE));
            // Nethergem
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.HEAD, Rarity.EPIC, "nether"));
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.CHEST, Rarity.EPIC, "nether"));
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.LEGS, Rarity.EPIC, "nether"));
            registry.register(new BaseGemArmorItem(GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.FEET, Rarity.EPIC, "nether"));
        }

        @SubscribeEvent
        public static void onBlockEntityRegistry(final RegistryEvent.Register<BlockEntityType<?>> event) {
            IForgeRegistry<BlockEntityType<?>> registry = event.getRegistry();

            registry.register(BlockEntityType.Builder.of(GeneratorBlockEntity::new, ModBlocks.GENERATOR).build(null).setRegistryName("generator"));
            registry.register(BlockEntityType.Builder.of(InfuserBlockEntity::new, ModBlocks.INFUSER).build(null).setRegistryName("infuser"));
        }

        @SubscribeEvent
        public static void onMenuRegistry(final RegistryEvent.Register<MenuType<?>> event) {
            IForgeRegistry<MenuType<?>> registry = event.getRegistry();

            registry.register(IForgeMenuType.create((windowId, inv, data) -> new GeneratorContainerMenu(windowId, inv)).setRegistryName("generator"));

            registry.register(IForgeMenuType.create(((windowId, inv, data) -> new InfuserContainerMenu(windowId, inv))).setRegistryName("infuser"));
        }

        @SubscribeEvent
        public static void onRecipeSerializerRegistry(final RegistryEvent.Register<RecipeSerializer<?>> event) {
            IForgeRegistry<RecipeSerializer<?>> registry = event.getRegistry();

            /* doing this first line here because Forge doesn't have its own registry for recipe types (yet?)
               TODO come back to this line of code if Forge has it's own registry for recipe types */
            ModBlocks.INFUSER_RECIPE_TYPE = RecipeType.register(SimpleGems.MODID + ":infuser");
            registry.register(new InfuserRecipe.Serializer());
        }
    }
}
