package com.ktraw.simplegems.items;

import com.ktraw.simplegems.items.armor.BaseGemArmorItem;
import com.ktraw.simplegems.items.rings.GemRing;
import com.ktraw.simplegems.items.tools.BaseGemAxe;
import com.ktraw.simplegems.items.tools.BaseGemHoe;
import com.ktraw.simplegems.items.tools.BaseGemPickaxe;
import com.ktraw.simplegems.items.tools.BaseGemShovel;
import com.ktraw.simplegems.items.tools.BaseGemSword;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {
    @ObjectHolder("simplegems:ruby")
    public static BaseItem RUBY;

    @ObjectHolder("simplegems:charged_emerald_dust")
    public static ChargedEmeraldDust CHARGED_EMERALD_DUST;

    @ObjectHolder("simplegems:gem")
    public static BaseItem GEM;

    @ObjectHolder("simplegems:gem_pickaxe")
    public static BaseGemPickaxe GEM_PICKAXE;

    @ObjectHolder("simplegems:gem_sword")
    public static BaseGemSword GEM_SWORD;

    @ObjectHolder("simplegems:gem_axe")
    public static BaseGemAxe GEM_AXE;

    @ObjectHolder("simplegems:gem_shovel")
    public static BaseGemShovel GEM_SHOVEL;

    @ObjectHolder("simplegems:gem_hoe")
    public static BaseGemHoe GEM_HOE;

    @ObjectHolder("simplegems:gem_helmet")
    public static BaseGemArmorItem GEM_HELMET;

    @ObjectHolder("simplegems:gem_chestplate")
    public static BaseGemArmorItem GEM_CHESTPLATE;

    @ObjectHolder("simplegems:gem_leggings")
    public static BaseGemArmorItem GEM_LEGGINGS;

    @ObjectHolder("simplegems:gem_boots")
    public static BaseGemArmorItem GEM_BOOTS;

    @ObjectHolder("simplegems:amethyst")
    public static BaseItem AMETHYST;

    @ObjectHolder("simplegems:purple_apple")
    public static PurpleApple PURPLE_APPLE;

    @ObjectHolder("simplegems:gold_ring")
    public static BaseItem GOLD_RING;

    @ObjectHolder("simplegems:gem_ring")
    public static GemRing GEM_RING;

    @ObjectHolder("simplegems:ring_of_haste")
    public static GemRing RING_OF_HASTE;

    @ObjectHolder("simplegems:ring_of_levitation")
    public static GemRing RING_OF_LEVITATION;
}
