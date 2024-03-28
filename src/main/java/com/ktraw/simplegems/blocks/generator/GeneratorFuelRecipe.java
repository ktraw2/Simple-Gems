package com.ktraw.simplegems.blocks.generator;

import net.minecraft.world.item.ItemStack;

public record GeneratorFuelRecipe(
        ItemStack fuel,
        int processTime,
        int energy
) {}
