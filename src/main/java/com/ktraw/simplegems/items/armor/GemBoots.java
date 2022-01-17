package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Rarity;

public class GemBoots extends ArmorItem {
    public GemBoots() {
        super(GemArmorMaterial.getMaterial(), EquipmentSlot.FEET, new Properties()
                                                                        .stacksTo(1)
                                                                        .tab(ModSetup.getSetup().getCreativeTab())
                                                                        .rarity(Rarity.RARE));
        setRegistryName("gem_boots");
    }
}
