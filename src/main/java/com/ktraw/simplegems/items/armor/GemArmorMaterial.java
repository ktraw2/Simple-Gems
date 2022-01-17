package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.items.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class GemArmorMaterial implements ArmorMaterial {
    private static final int[] DURABILITY = new int[]{13 * 40, 15 * 40, 16 * 40, 11 * 40};
    private static final int[] DAMAGE_REDUCTION = new int[]{4, 7, 9, 4};
    private static final GemArmorMaterial material = new GemArmorMaterial();

    private GemArmorMaterial() {}

    @Override
    public int getDurabilityForSlot(EquipmentSlot slotIn) {
        return DURABILITY[slotIn.getIndex()];
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slotIn) {
        return DAMAGE_REDUCTION[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return 25;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.GEM);
    }

    @Override
    public String getName() {
        return SimpleGems.MODID + ":gem";
    }

    @Override
    public float getToughness() {
        return 3f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }

    public static GemArmorMaterial getMaterial() {
        return material;
    }
}
