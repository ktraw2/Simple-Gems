package com.ktraw.simplegems.items;

import com.ktraw.simplegems.setup.ModSetup;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class EnderApple extends Item {

    public static Food foodProperties = new Food.Builder()
                                            .hunger(5)
                                            .saturation(11)
                                            .setAlwaysEdible()
                                            .effect(new EffectInstance(Effects.ABSORPTION, 2400, 2), 1)
                                            .effect(new EffectInstance(Effects.REGENERATION, 240, 2), 1)
                                            .effect(new EffectInstance(Effects.JUMP_BOOST, 4200, 2), 1)
                                            .build();

    public EnderApple() {
        super(new Item.Properties()
                    .maxStackSize(64)
                    .rarity(Rarity.RARE)
                    .group(ModSetup.getSetup().getCreativeTab())
                    .food(foodProperties));
        setRegistryName("ender_apple");
    }
}
