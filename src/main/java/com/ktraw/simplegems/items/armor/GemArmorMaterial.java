package com.ktraw.simplegems.items.armor;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.items.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class GemArmorMaterial implements IArmorMaterial {
    private static final int[] DURABILITY = new int[]{13 * 40, 15 * 40, 16 * 40, 11 * 40};
    private static final int[] DAMAGE_REDUCTION = new int[]{4, 7, 9, 4};
    private static final GemArmorMaterial material = new GemArmorMaterial();

    private GemArmorMaterial() {}

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return DURABILITY[slotIn.getIndex()];
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return DAMAGE_REDUCTION[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return 25;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(ModItems.GEM);
    }

    @Override
    public String getName() {
        return SimpleGems.MODID + ":gem";
    }

    @Override
    public float getToughness() {
        return 3f;
    }

    /**
     *
     * @return The knockback resistance
     */
    @Override
    public float func_230304_f_() {
        return 0;
    }

    public static GemArmorMaterial getMaterial() {
        return material;
    }
}
