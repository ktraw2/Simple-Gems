package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.items.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class GemItemTier implements Tier {
    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairMaterial;
    private static final GemItemTier tier = new GemItemTier(4, 2137, 14.0F, 4.0F, 25, () -> Ingredient.of(ModItems.GEM));

    private GemItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = Lazy.of(repairMaterialIn);
    }

    @Override
    public int getUses() { // TODO: getMaxUses?
        return this.maxUses;
    }

    @Override
    public float getSpeed() { // TODO: getSpeed?
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() { // TODO: getAttackDamage?
        return this.attackDamage;
    }

    @Override
    public int getLevel() { // TODO: getHarvestLevel?
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() { // TODO: getEnchantability?
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() { // TODO: getRepairMaterial?
        return this.repairMaterial.get();
    }

    public static GemItemTier getTier() {
        return tier;
    }
}
