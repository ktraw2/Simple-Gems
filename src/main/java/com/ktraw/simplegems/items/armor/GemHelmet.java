package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Rarity;

public class GemHelmet extends ArmorItem {
    public GemHelmet() {
        super(GemArmorMaterial.getMaterial(), EquipmentSlot.HEAD, new Properties()
                                                                        .stacksTo(1)
                                                                        .tab(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_helmet");
    }
}
