package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.registry.Items;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import static com.ktraw.simplegems.items.tools.GemItemTiers.GEM_ENCHANTABILITY;
import static com.ktraw.simplegems.items.tools.GemItemTiers.NETHERGEM_ENCHANTABILITY;

public class GemArmorMaterials {
    @Getter
    private static final GemArmorMaterial gemArmorMaterial = new GemArmorMaterial(
            new int[]{13 * 40, 15 * 40, 16 * 40, 11 * 40},
            new int[]{4, 7, 9, 4},
            GEM_ENCHANTABILITY,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            () -> Ingredient.of(Items.GEM.get()),
            3.0f,
            "gem",
            0.0f
    );

    @Getter
    private static final GemArmorMaterial nethergemArmorMaterial = new GemArmorMaterial(
            new int[]{13 * 50, 15 * 50, 16 * 50, 11 * 50},
            new int[]{5, 9, 10, 5},
            NETHERGEM_ENCHANTABILITY,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT),
            4.5f,
            "nethergem",
            0.15f
    );

    private GemArmorMaterials() {}

    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("ClassCanBeRecord")
    public static class GemArmorMaterial implements ArmorMaterial {
        private final int[] durabilityForSlot;
        private final int[] defenseForSlot;
        private final int enchantmentValue;
        private final SoundEvent equipSound;
        private final Lazy<Ingredient> repairIngredient;
        private final float toughness;
        private final String name;
        private final float knockbackResistance;

        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            return durabilityForSlot[slotIn.getIndex()];
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
            return defenseForSlot[slotIn.getIndex()];
        }

        @Override
        public String getName() {
            return SimpleGems.MODID + ":" + name;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }
}
