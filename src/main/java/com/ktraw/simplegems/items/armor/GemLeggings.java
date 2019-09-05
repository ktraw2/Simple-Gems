package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Rarity;

public class GemLeggings extends ArmorItem {
    public GemLeggings() {
        super(GemArmorMaterial.getMaterial(), EquipmentSlotType.LEGS, new Properties()
                                                                        .maxStackSize(1)
                                                                        .group(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_leggings");
    }
}
