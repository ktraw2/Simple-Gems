package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BaseGemArmorItem extends ArmorItem {
    private static String getNameSuffix(EquipmentSlot equipmentSlot) {
        return switch (equipmentSlot) {
            case MAINHAND -> "mainhand";
            case OFFHAND -> "offhand";
            case FEET -> "boots";
            case LEGS -> "leggings";
            case CHEST -> "chestplate";
            case HEAD -> "helmet";
        };
    }

    public static RegistryObject<BaseGemArmorItem> create(DeferredRegister<Item> registry, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Rarity rarity, String registryNamePrefix) {
        return registry.register(registryNamePrefix + "gem_" + getNameSuffix(equipmentSlot), () -> new BaseGemArmorItem(armorMaterial, equipmentSlot, rarity));
    }

    public static RegistryObject<BaseGemArmorItem> create(DeferredRegister<Item> registry, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Rarity rarity) {
        return create(registry, armorMaterial, equipmentSlot, rarity, "");
    }

    private BaseGemArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Rarity rarity) {
        super(armorMaterial, equipmentSlot, new Item.Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));
    }
}
