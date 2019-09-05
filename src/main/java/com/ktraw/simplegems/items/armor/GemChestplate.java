package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Rarity;

public class GemChestplate extends ArmorItem {
    public GemChestplate() {
        super(GemArmorMaterial.getMaterial(), EquipmentSlotType.CHEST, new Properties()
                                                                        .maxStackSize(1)
                                                                        .group(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_chestplate");
    }
}
