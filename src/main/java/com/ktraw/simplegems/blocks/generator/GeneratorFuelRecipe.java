package com.ktraw.simplegems.blocks.generator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;

@Getter
@RequiredArgsConstructor
public class GeneratorFuelRecipe {
    private final ItemStack fuel;
    private final int processTime;
    private final int energy;
}
