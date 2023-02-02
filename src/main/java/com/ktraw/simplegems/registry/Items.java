package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.items.BaseItem;
import com.ktraw.simplegems.items.ChargedEmeraldDust;
import com.ktraw.simplegems.items.PurpleApple;
import com.ktraw.simplegems.items.armor.BaseGemArmorItem;
import com.ktraw.simplegems.items.armor.GemArmorMaterials;
import com.ktraw.simplegems.items.rings.GemRing;
import com.ktraw.simplegems.items.tools.BaseGemAxe;
import com.ktraw.simplegems.items.tools.BaseGemHoe;
import com.ktraw.simplegems.items.tools.BaseGemPickaxe;
import com.ktraw.simplegems.items.tools.BaseGemShovel;
import com.ktraw.simplegems.items.tools.BaseGemSword;
import com.ktraw.simplegems.items.tools.GemItemTiers;
import com.ktraw.simplegems.util.mobeffects.MobEffectInstanceWrapper;
import com.ktraw.simplegems.util.mobeffects.MultiMobEffectProvider;
import com.ktraw.simplegems.util.mobeffects.SingleMobEffectProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Collection;

import static com.ktraw.simplegems.SimpleGems.POTION_TICKS;
import static com.ktraw.simplegems.SimpleGems.heavyRingAttributes;


public class Items {
    public static Collection<ItemStack> getAllRegisteredItems() {
        return ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .map(ItemStack::new)
                .toList();
    }

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleGems.MODID);

    /* Blocks as items */
    public static final RegistryObject<BlockItem> EMERALD_LAMP = createBlockItem(Blocks.EMERALD_LAMP);
    public static final RegistryObject<BlockItem> INVERTED_EMERALD_LAMP = createBlockItem(Blocks.INVERTED_EMERALD_LAMP);
    public static final RegistryObject<BlockItem> RUBY_ORE = createBlockItem(Blocks.RUBY_ORE);
    public static final RegistryObject<BlockItem> DEEPSLATE_RUBY_ORE = createBlockItem(Blocks.DEEPSLATE_RUBY_ORE);
    public static final RegistryObject<BlockItem> RUBY_BLOCK = createBlockItem(Blocks.RUBY_BLOCK);
    public static final RegistryObject<BlockItem> AMETHYST_BLOCK = createBlockItem(Blocks.AMETHYST_BLOCK);
    public static final RegistryObject<BlockItem> GEM_BLOCK = createBlockItem(Blocks.GEM_BLOCK, Rarity.RARE);
    public static final RegistryObject<BlockItem> GENERATOR = createBlockItem(Blocks.GENERATOR, Rarity.RARE);
    public static final RegistryObject<BlockItem> INFUSER = createBlockItem(Blocks.INFUSER);

    private static RegistryObject<BlockItem> createBlockItem(RegistryObject<? extends Block> block) {
        return createBlockItem(block, Rarity.COMMON);
    }

    private static RegistryObject<BlockItem> createBlockItem(RegistryObject<? extends Block> block, Rarity rarity) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()/*.tab(setup.getCreativeTab())*/.rarity(rarity)));
    }

    /* Regular items */
    public static RegistryObject<BaseItem> RUBY = ITEMS.register("ruby", BaseItem::new);
    public static RegistryObject<BaseItem> AMETHYST = ITEMS.register("amethyst", BaseItem::new);
    public static RegistryObject<BaseItem> GEM = ITEMS.register("gem", () -> new BaseItem(Rarity.RARE));
    public static RegistryObject<ChargedEmeraldDust> CHARGED_EMERALD_DUST = ITEMS.register("charged_emerald_dust", ChargedEmeraldDust::new);
    public static RegistryObject<PurpleApple> PURPLE_APPLE = ITEMS.register("purple_apple", PurpleApple::new);
    // Rings
    public static RegistryObject<BaseItem> GOLD_RING = ITEMS.register("gold_ring", () -> new BaseItem(1));
    public static RegistryObject<GemRing> GEM_RING = ITEMS.register("gem_ring", () -> new GemRing(null));
    public static RegistryObject<GemRing> RING_OF_HASTE = ITEMS.register("ring_of_haste", () -> new GemRing(new SingleMobEffectProvider(new MobEffectInstanceWrapper(MobEffects.DIG_SPEED, POTION_TICKS, 1))));
    public static RegistryObject<GemRing> RING_OF_LEVITATION = ITEMS.register("ring_of_levitation", () -> new GemRing(new SingleMobEffectProvider(new MobEffectInstanceWrapper(MobEffects.LEVITATION, POTION_TICKS))));
    public static RegistryObject<GemRing> RING_OF_VULNERABLE_STRENGTH = ITEMS.register("ring_of_vulnerable_strength", () -> new GemRing(new MultiMobEffectProvider(Arrays.asList(new MobEffectInstanceWrapper(MobEffects.DAMAGE_BOOST, POTION_TICKS, 1), new MobEffectInstanceWrapper(MobEffects.DAMAGE_RESISTANCE, POTION_TICKS, -5)))));
    public static RegistryObject<GemRing> RING_OF_HEAVY = ITEMS.register("ring_of_heavy", () -> new GemRing(new SingleMobEffectProvider(new MobEffectInstanceWrapper(MobEffects.MOVEMENT_SLOWDOWN, POTION_TICKS, 3)), heavyRingAttributes, Component.translatable("tooltip.simplegems.heavy").setStyle( Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)))));
    // Tools/Weapons
    // Gem
    public static RegistryObject<BaseGemPickaxe> GEM_PICKAXE = BaseGemPickaxe.create(ITEMS, GemItemTiers.getGemItemTier(), Rarity.RARE);
    public static RegistryObject<BaseGemSword> GEM_SWORD = BaseGemSword.create(ITEMS, GemItemTiers.getGemItemTier(), Rarity.RARE);
    public static RegistryObject<BaseGemAxe> GEM_AXE = BaseGemAxe.create(ITEMS, GemItemTiers.getGemItemTier(), Rarity.RARE);
    public static RegistryObject<BaseGemShovel> GEM_SHOVEL = BaseGemShovel.create(ITEMS, GemItemTiers.getGemItemTier(), Rarity.RARE);
    public static RegistryObject<BaseGemHoe> GEM_HOE = BaseGemHoe.create(ITEMS, GemItemTiers.getGemItemTier(), Rarity.RARE);
    // Nethergem
    public static RegistryObject<BaseGemPickaxe> NETHERGEM_PICKAXE = BaseGemPickaxe.create(ITEMS, GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemSword> NETHERGEM_SWORD = BaseGemSword.create(ITEMS, GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemAxe> NETHERGEM_AXE = BaseGemAxe.create(ITEMS, GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemShovel> NETHERGEM_SHOVEL = BaseGemShovel.create(ITEMS, GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemHoe> NETHERGEM_HOE = BaseGemHoe.create(ITEMS, GemItemTiers.getNetherGemItemTier(), Rarity.EPIC, "nether");
    // Armor
    // Gem
    public static RegistryObject<BaseGemArmorItem> GEM_HELMET = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.HEAD, Rarity.RARE);
    public static RegistryObject<BaseGemArmorItem> GEM_CHESTPLATE = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.CHEST, Rarity.RARE);
    public static RegistryObject<BaseGemArmorItem> GEM_LEGGINGS = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.LEGS, Rarity.RARE);
    public static RegistryObject<BaseGemArmorItem> GEM_BOOTS = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getGemArmorMaterial(), EquipmentSlot.FEET, Rarity.RARE);
    // Nethergem
    public static RegistryObject<BaseGemArmorItem> NETHERGEM_HELMET = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.HEAD, Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemArmorItem> NETHERGEM_CHESTPLATE = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.CHEST, Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemArmorItem> NETHERGEM_LEGGINGS = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.LEGS, Rarity.EPIC, "nether");
    public static RegistryObject<BaseGemArmorItem> NETHERGEM_BOOTS = BaseGemArmorItem.create(ITEMS, GemArmorMaterials.getNethergemArmorMaterial(), EquipmentSlot.FEET, Rarity.EPIC, "nether");

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
