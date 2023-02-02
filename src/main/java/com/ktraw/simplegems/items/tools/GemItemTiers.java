package com.ktraw.simplegems.items.tools;

import com.ktraw.simplegems.registry.Items;
import lombok.Getter;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

@Getter
public class GemItemTiers {
    public static final int GEM_ENCHANTABILITY = 25;
    public static final int NETHERGEM_ENCHANTABILITY = 30;

    @Getter
    private static final GemItemTier gemItemTier = new GemItemTier(4, 2137, 14.0F, 4.0F, GEM_ENCHANTABILITY, () -> Ingredient.of(Items.GEM.get()));

    @Getter
    private static final GemItemTier netherGemItemTier = new GemItemTier(4, 3542, 20.0F, 5.0F, NETHERGEM_ENCHANTABILITY, () -> Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT));

    private GemItemTiers() {}

    @Getter
    public static class GemItemTier implements Tier {
        private final int level; // harvest level
        private final int uses; // max uses
        private final float speed; // efficiency
        private final float attackDamageBonus; // attack damage
        private final int enchantmentValue; // enchantability
        private final Lazy<Ingredient> repairIngredient; // repairingredient

        private GemItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
            this.level = harvestLevelIn;
            this.uses = maxUsesIn;
            this.speed = efficiencyIn;
            this.attackDamageBonus = attackDamageIn;
            this.enchantmentValue = enchantabilityIn;
            this.repairIngredient = Lazy.of(repairMaterialIn);
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }
    }
}
