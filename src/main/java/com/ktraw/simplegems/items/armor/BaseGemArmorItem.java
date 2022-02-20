package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class BaseGemArmorItem extends ArmorItem {
    public BaseGemArmorItem(String registryName, EquipmentSlot equipmentSlot) {
        super(GemArmorMaterial.getMaterial(), equipmentSlot, new Item.Properties()
                .stacksTo(1)
                .tab(ModSetup.getSetup().getCreativeTab())
                .rarity(Rarity.RARE));

        setRegistryName(registryName);
    }
}
