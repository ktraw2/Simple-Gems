package com.ktraw.mymod.items.armor;

import com.ktraw.mymod.setup.ModSetup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Rarity;

public class GemHelmet extends ArmorItem {
    public GemHelmet() {
        super(GemArmorMaterial.getMaterial(), EquipmentSlotType.HEAD, new Properties()
                                                                        .maxStackSize(1)
                                                                        .group(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_helmet");
    }
}
