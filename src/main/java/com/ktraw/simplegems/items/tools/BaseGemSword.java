package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemSword extends SwordItem {

    private static final int ATTACK_DAMAGE_MODIFIER = 5;
    private static final float ATTACK_SPEED_MODIFIER = -2.4f;

    public static RegistryObject<BaseGemSword> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity, String registryNamePrefix) {
        return registry.register(registryNamePrefix + "gem_sword", () -> new BaseGemSword(itemTier, rarity));
    }

    public static RegistryObject<BaseGemSword> create(DeferredRegister<Item> registry, Tier itemTier, Rarity rarity) {
        return create(registry, itemTier, rarity, "");
    }

    private BaseGemSword(Tier itemTier, Rarity rarity) {
        super(itemTier, ATTACK_DAMAGE_MODIFIER, ATTACK_SPEED_MODIFIER, new Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
    }
}
