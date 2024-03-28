package com.ktraw.simplegems.items.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemArmorItem extends ArmorItem {
    public static RegistryObject<BaseGemArmorItem> create(
            final DeferredRegister<Item> registry,
            final ArmorMaterial armorMaterial,
            final ArmorItem.Type type,
            final Rarity rarity,
            final String registryNamePrefix
    ) {
        return registry.register(registryNamePrefix + "gem_" + type.getName(), () -> new BaseGemArmorItem(armorMaterial, type, rarity));
    }

    public static RegistryObject<BaseGemArmorItem> create(
            final DeferredRegister<Item> registry,
            final ArmorMaterial armorMaterial,
            final ArmorItem.Type type,
            final Rarity rarity
    ) {
        return create(registry, armorMaterial, type, rarity, "");
    }

    private BaseGemArmorItem(
            final ArmorMaterial armorMaterial,
            final ArmorItem.Type type,
            final Rarity rarity
    ) {
        super(armorMaterial, type, new Item.Properties()
                .stacksTo(1)
                .rarity(rarity));
    }
}
