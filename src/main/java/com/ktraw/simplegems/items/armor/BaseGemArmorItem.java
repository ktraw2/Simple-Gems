package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

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

    public BaseGemArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Rarity rarity, String registryNamePrefix) {
        super(armorMaterial, equipmentSlot, new Item.Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(rarity));

        setRegistryName(registryNamePrefix + "gem_" + getNameSuffix(equipmentSlot));
    }

    public BaseGemArmorItem(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Rarity rarity) {
        this(armorMaterial, equipmentSlot, rarity, "");
    }
}
