package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.items.ModItems;
import lombok.Getter;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

@Getter
public class GemItemTier implements Tier {
    private final int level; // harvest level
    private final int uses; // max uses
    private final float speed; // efficiency
    private final float attackDamageBonus; // attack damage
    private final int enchantmentValue; // enchantability
    private final Lazy<Ingredient> repairMaterial; // repairingredient

    @Getter
    private static final GemItemTier tier = new GemItemTier(4, 2137, 14.0F, 4.0F, 25, () -> Ingredient.of(ModItems.GEM));

    private GemItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.level = harvestLevelIn;
        this.uses = maxUsesIn;
        this.speed = efficiencyIn;
        this.attackDamageBonus = attackDamageIn;
        this.enchantmentValue = enchantabilityIn;
        this.repairMaterial = Lazy.of(repairMaterialIn);
    }

    @Override
    public Ingredient getRepairIngredient() { // TODO: getRepairMaterial?
        return this.repairMaterial.get();
    }
}
