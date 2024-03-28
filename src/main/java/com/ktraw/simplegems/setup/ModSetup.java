package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.registry.Items;
import lombok.Getter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

import java.lang.reflect.Method;

@Getter
public final class ModSetup {
    @Getter
    private static final ModSetup setup = new ModSetup();

    public void init() {
        try {
            final Method addMix = PotionBrewing.class.getDeclaredMethod("addMix", Potion.class, Item.class, Potion.class);
            addMix.setAccessible(true);
            addMix.invoke(null, Potions.AWKWARD, Items.RUBY.get(), Potions.LUCK);
        } catch (final Exception e) {
            System.err.println("Error adding custom potions.");
            e.printStackTrace();
        }
    }
}
