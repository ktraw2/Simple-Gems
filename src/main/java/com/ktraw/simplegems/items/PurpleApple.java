package com.ktraw.simplegems.items;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class PurpleApple extends Item {

    public static final FoodProperties foodProperties = new FoodProperties.Builder()
                                                            .nutrition(5)
                                                            .saturationMod(11)
                                                            .alwaysEat()
                                                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 2), 1)
                                                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 240, 2), 1)
                                                            .effect(() -> new MobEffectInstance(MobEffects.JUMP, 4200, 2), 1)
                                                            .build();

    public PurpleApple() {
        super(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.RARE)
                    .tab(ModSetup.getSetup().getCreativeTab())
                    .food(foodProperties));
    }
}